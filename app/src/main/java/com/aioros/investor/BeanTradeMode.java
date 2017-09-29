package com.aioros.investor;

/**
 * Created by aizhang on 2017/6/24.
 */

public class BeanTradeMode {
    public String mModeName;
    public String mModePara;
    public double mKeyRatio = 0;
    public double mKeyPoint = 0;
    public boolean mStatus = true;
    public int mDuration = 0;
    public String mAmount;
    public double mCost = 0;
    public double mRatio = 0;

    public BeanTradeMode(String name, String para, String amount) {
        super();
        mModeName = name;
        mModePara = para;
        mAmount = amount;
    }
}
