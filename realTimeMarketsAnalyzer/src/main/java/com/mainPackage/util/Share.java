package com.mainPackage.util;

/**
 * Created by claudiu on 02.05.2017.
 */
public class Share {

    private String name;
    private float y, price;
    private String companyName;
    private int shares;
    private String currency;
    private String portofolio;

    public  Share(String name, float y, String companyName, int shares,
                  String currency, float price, String portofolio) {
        this.name = name;
        this.y = y;
        this.companyName = companyName;
        this.shares = shares;
        this.currency = currency;
        this.price = price;
        this.portofolio = portofolio;
    }
    public String getName() {
        return name;
    }

    public String getCompanyName() {
        return companyName;
    }

    public float getY() {
        return y;
    }

    public int getShares() {
        return shares;
    }

    public void setY(float y) {
        this.y = y;
    }

    public String getCurrency() {
        return currency;
    }

    public float getPrice() {
        return price;
    }

    public String getPortofolio() {
        return portofolio;
    }

    public void setShares(int shares) {
        this.shares = shares;
    }
}
