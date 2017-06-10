package mainservices;

/**
 * Created by claudiu on 08.04.2017.
 */
public class Symbol {

    private String symbol;
    private String companyName;
    private float price;

    public Symbol(String symbol, String companyName, float price) {
        this.symbol = symbol;
        this.companyName = companyName;
        this.price = price;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getCompanyName() {
        return companyName;
    }

    public float getPrice() {
        return price;
    }
}
