package com.aioros.investor;

/**
 * Created by aizhang on 2017/6/24.
 */

public class BeanTradeMode {
    public String mModeName;
    public String mModePara;
    public String mKeyRatio;
    public String mKeyPoint;
    public boolean mStatus;
    public String mDuration;
    public String mAmount;

    public BeanTradeMode(String name, String para, boolean status, String point, String ratio, String amount, String duration) {
        super();
        mModeName = name;
        mModePara = para;
        mStatus = status;
        mKeyRatio = ratio;
        mKeyPoint = point;
        mDuration = duration;
        mAmount = amount;
    }

    @Override
    public String toString() {
        return "BeanTradeMode [mModeName=" + mModeName + ", mModePara=" + mModePara
                + ", mStatus=" + mStatus + ", mKeyPoint=" + mKeyPoint
                + ", mDuration=" + mDuration + "]";
    }
}
