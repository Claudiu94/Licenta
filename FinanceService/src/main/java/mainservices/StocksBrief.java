package mainservices;

/**
 * Created by claudiu on 08.04.2017.
 */
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import java.io.IOException;
import java.util.ArrayList;

public class StocksBrief
{
    private static String[] symbolsList = new String[]{"INTC", "BABA", "TSLA", "AIR.PA", "YHOO", "GOOG"};
    private ArrayList<Symbol> symbols = new ArrayList<>();

    public StocksBrief() throws IOException {
        for (String sym : symbolsList) {
            Stock stock = YahooFinance.get(sym);
            stock.getCurrency();
            
            symbols.add(new Symbol(sym, stock.getName(), stock.getQuote().getPrice().floatValue()));
        }
    }

    public ArrayList<Symbol> getSymbols() {
        return symbols;
    }
}
