package com.mainPackage.util;

/**
 * Created by cozafiu on 13.04.2017.
 */
public class Symbol {

  private String symbol;
  private String companyName;
  private float price;
  private String currency;

  public Symbol(String symbol, String companyName, float price, String currency) {
    this.symbol = symbol;
    this.companyName = companyName;
    this.price = price;
    this.currency = currency;
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

  public String getCurrency() {
    return currency;
  }
}
