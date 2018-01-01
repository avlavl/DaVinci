package com.aioros.investor;

/**
 * Created by aizhang on 2017/6/19.
 */

public class BeanInvest {
    public int mStartPoint;
    public double mSlope;
    public double mWinLevel;
    public double mDiffCoef;
    public int mDivisor;

    public String mQuota = "--";
    public String mRealPoint = "--";
    public String mBasePoint = "--";
    public String mProperty = "--";
    public String mYield = "--";
    public String mKeyPoint = "--";
    public String mKeyRatio = "--";

    public BeanInvest(int startPoint, double slope, double winLevel, double diffCoef, int divisor) {
        super();
        mStartPoint = startPoint;
        mSlope = slope;
        mWinLevel = winLevel;
        mDiffCoef = diffCoef;
        mDivisor = divisor;
    }
}