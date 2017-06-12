package com.aioros.investor;

public class StockBean {
    private String stockName;
    private String stockCode;
    private String stockValue;
    private String stockRatio;
    private String stockScope;

    public StockBean(String name, String code, String value, String scope, String ratio) {
        super();
        stockName = name;
        stockCode = code;
        stockValue = value;
        stockScope = scope;
        stockRatio = ratio;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String name) {
        stockName = name;
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String code) {
        stockCode = code;
    }

    public String getStockValue() {
        return stockValue;
    }

    public void setStockValue(String value) {
        stockValue = value;
    }

    public String getStockScope() {
        return stockScope;
    }

    public void setStockScope(String scope) {
        stockScope = scope;
    }

    public String getStockRatio() {
        return stockRatio;
    }

    public void setStockRatio(String ratio) {
        stockRatio = ratio;
    }


    @Override
    public String toString() {
        return "StockBean [stockName=" + stockName + ", stockCode=" + stockCode
                + ", stockValue=" + stockValue + ", stockScope=" + stockScope
                + ", stockRatio=" + stockRatio + "]";
    }
}
