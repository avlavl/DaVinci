package com.aioros.investor;

public class BeanStock {
    public String mStockName;
    public String mStockCode;
    public String mStockValue;
    public String mStockRatio;
    public String mStockScope;

    public BeanStock(String name, String code, String value, String scope, String ratio) {
        super();
        mStockName = name;
        mStockCode = code;
        mStockValue = value;
        mStockScope = scope;
        mStockRatio = ratio;
    }

    @Override
    public String toString() {
        return "BeanStock [mStockName=" + mStockName + ", mStockCode=" + mStockCode
                + ", mStockValue=" + mStockValue + ", mStockScope=" + mStockScope
                + ", mStockRatio=" + mStockRatio + "]";
    }
}
