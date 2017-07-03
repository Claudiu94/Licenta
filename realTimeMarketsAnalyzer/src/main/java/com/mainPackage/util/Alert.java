package com.mainPackage.util;

/**
 * Created by claudiu on 03.07.2017.
 */
public class Alert {
    private String message, price, symbol, company, currentPrice;

    public Alert(String symbol, String company, String price,
                 String currentPrice, String message) {
        this.symbol = symbol;
        this.company = company;
        this.price = price;
        this.currentPrice = currentPrice;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getPrice() {
        return price;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getCompany() {
        return company;
    }

    public String getCurrentPrice() {
        return currentPrice;
    }
}
