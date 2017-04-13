package com.mainPackage.util;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by cozafiu on 13.04.2017.
 */
public class StocksBrief {

  private static String[] symbolsList = new String[]{"INTC", "BABA", "TSLA", "AIR.PA", "YHOO", "GOOG", "^GSPC"};
  private ArrayList<Symbol> symbols = new ArrayList();

  public StocksBrief() throws IOException {
    for (String sym : symbolsList) {
      Stock stock = YahooFinance.get(sym);

      symbols.add(new Symbol(sym, stock.getName(), stock.getQuote().getPrice().floatValue(), stock.getCurrency()));
    }
  }

  public ArrayList<Symbol> getCompanies() {
    return symbols;
  }
}
