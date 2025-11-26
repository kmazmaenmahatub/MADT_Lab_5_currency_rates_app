package com.kmazmaenmahatub.currencyratesapp;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Parser {

    public List<CurrencyItem> parseJsonData(String jsonData) {
        List<CurrencyItem> currencyList = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONObject rates = jsonObject.getJSONObject("rates");

            Iterator<String> keys = rates.keys();
            while (keys.hasNext()) {
                String currencyCode = keys.next();
                double rate = rates.getDouble(currencyCode);
                currencyList.add(new CurrencyItem(currencyCode, rate, currencyCode));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return currencyList;
    }
}