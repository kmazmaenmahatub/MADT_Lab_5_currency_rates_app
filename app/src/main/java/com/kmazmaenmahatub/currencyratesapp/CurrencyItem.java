package com.kmazmaenmahatub.currencyratesapp;

public class CurrencyItem {
    private String currencyCode;
    private double exchangeRate;
    private String currencyName;

    public CurrencyItem(String currencyCode, double exchangeRate, String currencyName) {
        this.currencyCode = currencyCode;
        this.exchangeRate = exchangeRate;
        this.currencyName = currencyName;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public double getExchangeRate() {
        return exchangeRate;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    @Override
    public String toString() {
        return currencyCode + " - " + String.format("%.3f", exchangeRate);
    }
}