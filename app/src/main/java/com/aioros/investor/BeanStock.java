package com.aioros.investor;

public class BeanStock {
    private String mStockName;
    private String mStockCode;
    private String mStockValue;
    private String mStockRatio;
    private String mStockScope;

    public BeanStock(String name, String code, String value, String scope, String ratio) {
        super();
        mStockName = name;
        mStockCode = code;
        mStockValue = value;
        mStockScope = scope;
        mStockRatio = ratio;
    }

    public String getmStockName() {
        return mStockName;
    }

    public void setmStockName(String name) {
        mStockName = name;
    }

    public String getmStockCode() {
        return mStockCode;
    }

    public void setmStockCode(String code) {
        mStockCode = code;
    }

    public String getmStockValue() {
        return mStockValue;
    }

    public void setmStockValue(String value) {
        mStockValue = value;
    }

    public String getmStockScope() {
        return mStockScope;
    }

    public void setmStockScope(String scope) {
        mStockScope = scope;
    }

    public String getmStockRatio() {
        return mStockRatio;
    }

    public void setmStockRatio(String ratio) {
        mStockRatio = ratio;
    }


    @Override
    public String toString() {
        return "BeanStock [mStockName=" + mStockName + ", mStockCode=" + mStockCode
                + ", mStockValue=" + mStockValue + ", mStockScope=" + mStockScope
                + ", mStockRatio=" + mStockRatio + "]";
    }
}
