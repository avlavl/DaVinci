package com.aioros.investor;

/**
 * Created by aizhang on 2017/6/19.
 */

public class InvestBean {
    private int mStartPoint;
    private double mSlope;
    private int mWinLevel;
    private double mDiffCoef;
    private int mDivisor;

    private String mQuota = "--";
    private String mRealPoint = "--";
    private String mBasePoint = "--";
    private String mProperty = "--";
    private String mYield = "--";
    private String mKeyPoint = "--";
    private String mKeyRatio = "--";

    public InvestBean(int startPoint, double slope, int winLevel, double diffCoef, int divisor) {
        super();
        mStartPoint = startPoint;
        mSlope = slope;
        mWinLevel = winLevel;
        mDiffCoef = diffCoef;
        mDivisor = divisor;
    }

    public int getmStartPoint() {
        return mStartPoint;
    }

    public void setmStartPoint(int mStartPoint) {
        this.mStartPoint = mStartPoint;
    }

    public double getmSlope() {
        return mSlope;
    }

    public void setmSlope(double mSlope) {
        this.mSlope = mSlope;
    }

    public double getmDiffCoef() {
        return mDiffCoef;
    }

    public void setmDiffCoef(double mDiffCoef) {
        this.mDiffCoef = mDiffCoef;
    }

    public int getmWinLevel() {
        return mWinLevel;
    }

    public void setmWinLevel(int mWinLevel) {
        this.mWinLevel = mWinLevel;
    }

    public int getmDivisor() {
        return mDivisor;
    }

    public void setmDivisor(int mDivisor) {
        this.mDivisor = mDivisor;
    }

    public String getmQuota() {
        return mQuota;
    }

    public void setmQuota(String quota) {
        mQuota = quota;
    }

    public String getmRealPoint() {
        return mRealPoint;
    }

    public void setmRealPoint(String point) {
        mRealPoint = point;
    }

    public String getmBasePoint() {
        return mBasePoint;
    }

    public void setmBasePoint(String point) {
        mBasePoint = point;
    }

    public String getmProperty() {
        return mProperty;
    }

    public void setmProperty(String property) {
        mProperty = property;
    }

    public String getmYield() {
        return mYield;
    }

    public void setmYield(String yield) {
        mYield = yield;
    }


    public String getmKeyPoint() {
        return mKeyPoint;
    }

    public void setmKeyPoint(String mKeyPoint) {
        this.mKeyPoint = mKeyPoint;
    }

    public String getmKeyRatio() {
        return mKeyRatio;
    }

    public void setmKeyRatio(String mKeyRatio) {
        this.mKeyRatio = mKeyRatio;
    }

    @Override
    public String toString() {
        return "StockBean [mQuota=" + mQuota + ", mRealPoint=" + mRealPoint
                + ", mBasePoint=" + mBasePoint + ", mYield=" + mYield
                + ", mProperty=" + mProperty + "]";
    }
}