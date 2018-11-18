package com.aioros.investor;

/**
 * Created by zhangxr on 2018/11/18.
 */

public class BeanInvestRecord {
    public String mDate;
    public double mPrice;
    public double mCost;
    public double mQuota;
    public double mAmount;
    public double mYield;
    public double mRatio;

    public BeanInvestRecord(String date, double price, double cost, double quota, double amount, double yield, double ratio) {
        super();
        mDate = date;
        mPrice = price;
        mCost = cost;
        mQuota = quota;
        mAmount = amount;
        mYield = yield;
        mRatio = ratio;
    }
}
