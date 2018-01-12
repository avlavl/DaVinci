package com.aioros.investor;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aizhang on 2017/6/7.
 */

public class FragmentInvest extends BaseFragment {
    private static final String TAG = "FragmentInvest";
    private MainActivity mMainActivity;
    private AdapterPagerInvest mAdapterPager;
    private List<BeanInvest> mBeanInvestList = new ArrayList<BeanInvest>();
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private FileUtility fileUtility = new FileUtility();
    public String[][] mMarketDatas;
    private String mTabTitles[] = new String[]{"申万证券", "养老产业"};
    private int[] indexArray = new int[]{7, 5};
    private int[] weeksArray = new int[2];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate------");
        mMainActivity = (MainActivity) getActivity();
        mMarketDatas = mMainActivity.mMarketDatas;
        mBeanInvestList.add(new BeanInvest(1000, 7, 1.5, 100, 0.2));
        mBeanInvestList.add(new BeanInvest(1000, 7, 1.5, 25, 2));
        mBeanInvestList.add(new BeanInvest(1400, 10, 1.5, 20, 0.2));
        mBeanInvestList.add(new BeanInvest(1400, 10, 1.5, 25, 2));

        for (int i = 0; i < indexArray.length; i++) {
            fileUtility.importDataFile("investor/data/W" + mTabTitles[i] + ".txt");
            weeksArray[i] = fileUtility.rows;
            if (weeksArray[i] > 0) {
                for (int j = 0; j < 2; j++) {
                    mBeanInvestList.get(2 * i + j).mRealPoint = Double.parseDouble(mMarketDatas[indexArray[i]][1]);
                    StrategyInvest strategyInvest = new StrategyInvest(fileUtility);
                    strategyInvest.sysInvestEva(mBeanInvestList.get(2 * i + j));
                    double basePoint = strategyInvest.basePoints[strategyInvest.items - 1] + mBeanInvestList.get(2 * i + j).mSlope;
                    mBeanInvestList.get(2 * i + j).mBasePoint = basePoint;
                    double diffRate = mBeanInvestList.get(2 * i + j).mRealPoint / basePoint;
                    double investAmount = (basePoint / mBeanInvestList.get(2 * i + j).mDivisor) / Math.pow(diffRate, mBeanInvestList.get(2 * i + j).mDiffCoef);
                    mBeanInvestList.get(2 * i + j).mQuota = (diffRate <= 1) ? investAmount : 0;
                    mBeanInvestList.get(2 * i + j).mTimes = strategyInvest.getInvestTimes();
                    mBeanInvestList.get(2 * i + j).mCurrentCost = strategyInvest.getCurrentCost();
                    mBeanInvestList.get(2 * i + j).mProperty = strategyInvest.getCurrentAsset();
                    mBeanInvestList.get(2 * i + j).mYield = strategyInvest.getCurrentYield();
                    mBeanInvestList.get(2 * i + j).mKeyPoint = strategyInvest.getKeyPoint();
                    mBeanInvestList.get(2 * i + j).mKeyRatio = strategyInvest.getKeyRatio();
                }
            }
        }

        // 在主线程中声明一个消息处理对象Handler
        mMainActivity.mInvestHandler = new Handler() {
            // 重载消息处理方法，用于接收和处理WorkerThread发送的消息
            @Override
            public void handleMessage(Message msg) {
                mMarketDatas = (String[][]) msg.obj;
                for (int i = 0; i < indexArray.length; i++) {
                    if (weeksArray[i] > 0) {
                        for (int j = 0; j < 2; j++) {
                            mBeanInvestList.get(2 * i + j).mRealPoint = Double.parseDouble(mMarketDatas[indexArray[i]][1]);
                            double basePoint = mBeanInvestList.get(2 * i + j).mStartPoint + weeksArray[i] * mBeanInvestList.get(2 * i + j).mSlope;
                            mBeanInvestList.get(2 * i + j).mBasePoint = basePoint;
                            double diffRate = mBeanInvestList.get(2 * i + j).mRealPoint / basePoint;
                            double investAmount = (basePoint / mBeanInvestList.get(2 * i + j).mDivisor) / Math.pow(diffRate, mBeanInvestList.get(2 * i + j).mDiffCoef);
                            mBeanInvestList.get(2 * i + j).mQuota = (diffRate <= 1) ? investAmount : 0;
                        }
                    }
                }
                mAdapterPager.notifyDataSetChanged();
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_invest, container, false);
        Log.d(TAG, "onCreateView---->");
        mFragmentManager = getActivity().getFragmentManager();
        mAdapterPager = new AdapterPagerInvest(mMainActivity, mBeanInvestList);
        mViewPager = (ViewPager) view.findViewById(R.id.viewPagerInvest);
        mViewPager.setAdapter(mAdapterPager);
        mTabLayout = (TabLayout) view.findViewById(R.id.tabLayoutInvest);
        mTabLayout.setupWithViewPager(mViewPager);
        mAdapterPager.notifyDataSetChanged();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.currFragTag = Constant.FRAGMENT_FLAG_INVEST;
    }
}
