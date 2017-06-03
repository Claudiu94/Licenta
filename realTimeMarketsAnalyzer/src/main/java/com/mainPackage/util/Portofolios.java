package com.mainPackage.util;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.mainPackage.database.ConnectionToDB;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by claudiu on 03.06.2017.
 */
public class Portofolios {
    @Autowired
    ConnectionToDB connectionToDB;
    @Autowired
    StocksBrief stocksBrief;

    private Cache<String, SharesList> cachedData;

    @PostConstruct
    public void init() {
        cachedData = CacheBuilder.newBuilder().maximumSize(1000).expireAfterAccess(10, TimeUnit.MINUTES).build();
    }

    public SharesList retreiveSharesFromDb(String user) throws ExecutionException {
        return cachedData.get(user, () -> {
            int userId = connectionToDB.getId(user);
            List<Map<String, Object>> data = connectionToDB.getShares(userId);
            float sum = 0;
            List<Share> userShares = new ArrayList<>();
            List<Symbol> symbolList = new ArrayList<Symbol>(stocksBrief.getCompanies());

            for (Map<String, Object> map : data) {
                int shares = (Integer)map.get("Shares");
                Symbol symbol = stocksBrief.retreiveSymbolFromName(map.get("Symbol").toString());

                symbolList.remove(symbol);
                userShares.add(new Share(symbol.getSymbol(), symbol.getPrice() * shares,
                        symbol.getCompanyName(), shares, symbol.getCurrency(), symbol.getPrice()));
                sum += symbol.getPrice() * shares;
            }

            for (int i = 0; i < userShares.size(); i++) {
                userShares.get(i).setY(userShares.get(i).getY() * 100 / sum);
            }

            for (Symbol symbol : symbolList) {
                userShares.add(new Share(symbol.getSymbol(), 0,
                        symbol.getCompanyName(), 0, symbol.getCurrency(), symbol.getPrice()));
            }

            return new SharesList(userShares);
        });
    }

    public void invalidateortofoliosCache() {
        cachedData.invalidateAll();
    }
}
