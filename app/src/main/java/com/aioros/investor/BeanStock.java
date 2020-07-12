package com.aioros.investor;

public class BeanStock {
    public String mStockName;
    public String mStockCode;
    public String mStockValue;
    public String mStockRatio;
    public String mStockScope;
    public String mStockProb;

    public BeanStock(String name, String code, String value, String scope, String ratio) {
        super();
        mStockName = name;
        mStockCode = code;
        mStockValue = value;
        mStockScope = scope;
        mStockRatio = ratio;
    }

    public BeanStock(String name, String code, String value, String scope, String ratio, String prob) {
        super();
        mStockName = name;
        mStockCode = code;
        mStockValue = value;
        mStockScope = scope;
        mStockRatio = ratio;
        mStockProb = prob;
    }

    public String getDailyRate() {
        double value = Double.parseDouble(mStockValue);
        double rate = (value < 100) ? 10000 * (100 - value) / value : 100 * (value - 100);
        String dailyRate = String.format("万 %.1f", rate);
        return dailyRate;
    }

    public String getAnualRatio() {
        double value = Double.parseDouble(mStockValue);
        double ratio = (value < 100) ? 36500 * (100 - value) / value : 365 * (value - 100);
        String anualRatio = String.format("%.2f%%", ratio);
        return anualRatio;
    }
}
