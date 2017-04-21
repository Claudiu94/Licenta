package com.mainPackage.util;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by cozafiu on 13.04.2017.
 */
public class StocksBrief {

  private static String[] symbolsList = new String[]{"ALTR", "BABA", "TSLA", "AIR.PA", "YHOO", "GOOG", "^GSPC"};
  private ArrayList<Symbol> symbols = new ArrayList();

  public StocksBrief(ArrayList<String> symList) throws IOException {
    String [] array = symList.toArray(new String [symList.size()]);
    Map<String, Stock> stocksMap = YahooFinance.get(array);

    for (Map.Entry<String, Stock> entry : stocksMap.entrySet()) {
      String sym = entry.getKey();
      Stock stock = entry.getValue();

      if (stock != null && stock.getQuote().getPrice() != null) {
        symbols.add(new Symbol(sym, stock.getName(), stock.getQuote().getPrice().floatValue(), stock.getCurrency()));
      }
    }
  }

  public ArrayList<Symbol> getCompanies() {
    return symbols;
  }
}
