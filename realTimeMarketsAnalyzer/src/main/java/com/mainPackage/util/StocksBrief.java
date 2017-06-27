package com.mainPackage.util;

import org.springframework.beans.factory.annotation.Autowired;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by cozafiu on 13.04.2017.
 */
public class StocksBrief {
  @Autowired
  JsonParser jsonParser;

  private static String[] symbolsList = new String[]{"ALTR", "BABA", "TSLA", "AIR.PA", "YHOO", "GOOG", "^GSPC"};
  private ArrayList<Symbol> symbols = new ArrayList();

  @PostConstruct
  public void init() throws IOException {
    ArrayList<String> symList = jsonParser.getSymbolys();
    String [] array = symList.toArray(new String [symList.size()]);
//    System.out.println(array);
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

  public Symbol retreiveSymbolFromName(String symbolName) {

    return symbols.stream()
            .filter(symbol -> symbol.getSymbol().equals(symbolName))
            .findFirst()
            .orElse(null);
  }

  public String retreiveNameForSymbol(String symName) {

    Symbol symbol = symbols.stream()
            .filter(sym -> sym.getSymbol().equals(symName))
            .findFirst()
            .orElse(null);

    return symbol != null ? symbol.getCompanyName() : null;
  }

  public float retreivePriceForSymbol(String symName) {

    Symbol symbol = symbols.stream()
            .filter(sym -> sym.getSymbol().equals(symName))
            .findFirst()
            .orElse(null);

    return symbol != null ? symbol.getPrice() : -1;
  }

  public String retreiveCurrencyForSymbol(String symName) {

    Symbol symbol = symbols.stream()
            .filter(sym -> sym.getSymbol().equals(symName))
            .findFirst()
            .orElse(null);

    return symbol != null ? symbol.getCurrency() : "USD";
  }
}
