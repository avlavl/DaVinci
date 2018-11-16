package com.aioros.investor;

/**
 * Created by zhangxr on 2018/11/13.
 */

public class BeanTradeRecord {
    public String mBpDate;
    public String mSpDate;
    public int mDays;
    public double mBpVal;
    public double mSpVal;
    public double mYield;
    public double mRatio;

    public BeanTradeRecord(String bpdate, String spDate, int days, double bpVal, double spVal, double yield, double ratio) {
        super();
        mBpDate = bpdate;
        mSpDate = spDate;
        mDays = days;
        mBpVal = bpVal;
        mSpVal = spVal;
        mYield = yield;
        mRatio = ratio;
    }
}
