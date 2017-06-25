package com.aioros.investor;

/**
 * Created by aizhang on 2017/6/24.
 */

public class BeanTradeMode {
    public String mModeName;
    public String mModePara;
    public String mKeyRatio = "--";
    public String mKeyPoint = "--";
    public boolean mStatus = true;
    public String mDuration = "--";
    public String mAmount;

    public BeanTradeMode(String name, String para, String amount) {
        super();
        mModeName = name;
        mModePara = para;
        mAmount = amount;
    }

    @Override
    public String toString() {
        return "BeanTradeMode [mModeName=" + mModeName + ", mModePara=" + mModePara
                + ", mStatus=" + mStatus + ", mKeyPoint=" + mKeyPoint
                + ", mDuration=" + mDuration + "]";
    }
}
