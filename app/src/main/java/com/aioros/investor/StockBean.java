package com.aioros.investor;

public class StockBean {
    private int photoId;
    private String stockName;
    private String stockCode;
    private String stockValue;
    private String stockRatio;
    private String stockScope;

    public StockBean(int phoId, String name, String code, String value, String ratio, String scope) {
        super();
        photoId = phoId;
        stockName = name;
        stockCode = code;
        stockValue = value;
        stockRatio = ratio;
        stockScope = scope;
    }

    public int getPhotoId() {
        return photoId;
    }

    public void setPhotoId(int phoId) {
        photoId = phoId;
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

    public String getStockRatio() {
        return stockRatio;
    }

    public void setStockRatio(String ratio) {
        stockRatio = ratio;
    }

    public String getStockScope() {
        return stockScope;
    }

    public void setStockScope(String scope) {
        stockScope = scope;
    }

    @Override
    public String toString() {
        return "StockBean [photoId=" + photoId + ", stockName=" + stockName
                + ", stockCode=" + stockCode + ", stockValue=" + stockValue
                + ", stockRatio=" + stockRatio + ", stockScope=" + stockScope + "]";
    }
}
