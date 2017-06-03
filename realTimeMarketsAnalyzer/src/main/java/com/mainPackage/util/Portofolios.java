package com.mainPackage.util;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.mainPackage.database.ConnectionToDB;
import org.springframework.beans.factory.annotation.Autowired;
import sun.security.provider.SHA;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
                String portofolio = map.get("PortofolioName").toString();

                symbolList.remove(symbol);
                userShares.add(new Share(symbol.getSymbol(), symbol.getPrice() * shares,
                        symbol.getCompanyName(), shares, symbol.getCurrency(), symbol.getPrice(),
                        portofolio));
                sum += symbol.getPrice() * shares;
            }

            for (int i = 0; i < userShares.size(); i++) {
                userShares.get(i).setY(userShares.get(i).getY() * 100 / sum);
            }

            for (Symbol symbol : symbolList) {
                userShares.add(new Share(symbol.getSymbol(), 0,
                        symbol.getCompanyName(), 0, symbol.getCurrency(), symbol.getPrice(), ""));
            }

            return new SharesList(userShares);
        });
    }

    public SharesList retreiveSharesFromDb(String user, String portofolio) throws ExecutionException {
        List<Share> portofolioShares = new ArrayList<>();
        SharesList allShares = retreiveSharesFromDb(user);
        List<Share> formOtherPortofolios = new ArrayList<>();

        portofolioShares = allShares.getSharesList().stream()
                .filter(share -> {
                    if (share.getPortofolio() != null)
                        if (share.getPortofolio().equals(portofolio)
                            || share.getShares() == 0)
                            return true;
                        else
                            formOtherPortofolios.add(share);
                    return false;
                })
                .collect(Collectors.toList());

        for (Share share : formOtherPortofolios) {
            Share share1 = new Share(share.getName(), 0, share.getCompanyName(), 0,
                    share.getCurrency(), share.getPrice(), "");

            portofolioShares.add(share1);
        }
        return new SharesList(portofolioShares);
    }

    public void invalidateortofoliosCache() {
        cachedData.invalidateAll();
    }
}
