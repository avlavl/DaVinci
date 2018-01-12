package com.aioros.investor;

/**
 * Created by aizhang on 2017/6/19.
 */

public class BeanInvest {
    public int mStartPoint;
    public double mSlope;
    public double mDiffCoef;
    public int mDivisor;
    public double mWinLevel;

    public double mQuota = 0;
    public double mRealPoint = 0;
    public double mBasePoint = 0;
    public double mProperty = 0;
    public double mYield = 0;
    public double mKeyPoint = 0;
    public double mKeyRatio = 0;
    public int mTimes = 0;
    public double mCurrentCost = 0;
    public double mTotalNumber = 0;

    public BeanInvest(int startPoint, double slope, double diffCoef, int divisor, double winLevel) {
        super();
        mStartPoint = startPoint;
        mSlope = slope;
        mDiffCoef = diffCoef;
        mDivisor = divisor;
        mWinLevel = winLevel;
    }
}