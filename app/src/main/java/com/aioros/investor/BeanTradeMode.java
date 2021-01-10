package com.aioros.investor;

import java.util.ArrayList;

/**
 * Created by aizhang on 2017/6/24.
 */

public class BeanTradeMode {
    public String mModeName;
    public String mModePara;
    public double mKeyRatio = 0;
    public double mKeyPoint = 0;
    public double mBSPoint = 0;
    public double mPosRate = 0;
    public double mMeanDate = 0;
    public boolean mStatus = true;
    public int mDuration = 0;
    public String mAmount;
    public ArrayList<Integer> bpIdxList = new ArrayList<>();
    public ArrayList<Integer> spIdxList = new ArrayList<>();

    public BeanTradeMode(String name, String para, String amount) {
        super();
        mModeName = name;
        mModePara = para;
        mAmount = amount;
    }
}
