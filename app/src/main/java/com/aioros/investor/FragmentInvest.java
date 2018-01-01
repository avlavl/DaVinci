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
    private int[] indexArray = new int[]{8, 6};
    private int[] weeksArray = new int[2];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate------");
        mMainActivity = (MainActivity) getActivity();
        mMarketDatas = mMainActivity.mMarketDatas;
        mBeanInvestList.add(new BeanInvest(1000, 7, 0.2, 1.5, 20));
        mBeanInvestList.add(new BeanInvest(1400, 10, 0.2, 1.5, 20));

        for (int i = 0; i < indexArray.length; i++) {
            mBeanInvestList.get(i).mRealPoint = mMarketDatas[indexArray[i]][1];
            fileUtility.importDataFile("investor/data/W" + mTabTitles[i] + ".txt");
            weeksArray[i] = fileUtility.rows;
            if (weeksArray[i] > 0) {
                StrategyInvest strategyInvest = new StrategyInvest(fileUtility);
                strategyInvest.sysInvestEva(mBeanInvestList.get(i));
                double basePoint = strategyInvest.basePoints[strategyInvest.items - 1];
                mBeanInvestList.get(i).mBasePoint = Double.toString(basePoint);
                double diffRate = Double.parseDouble(mBeanInvestList.get(i).mRealPoint) / basePoint;
                double divisor = mBeanInvestList.get(i).mDivisor;
                double investAmount = (basePoint / divisor) / Math.pow(diffRate, mBeanInvestList.get(i).mDiffCoef);
                mBeanInvestList.get(i).mQuota = (diffRate <= 1) ? String.format("%.2f", investAmount) : "无需投资";
                mBeanInvestList.get(i).mProperty = String.format("%d", (int) strategyInvest.getCurrentAsset());
                mBeanInvestList.get(i).mYield = String.format("%.2f%%", strategyInvest.getCurrentYield());
                mBeanInvestList.get(i).mKeyPoint = String.format("%.2f", strategyInvest.getKeyPoint());
                mBeanInvestList.get(i).mKeyRatio = String.format("%.2f%%", strategyInvest.getKeyRatio());
            }
        }

        // 在主线程中声明一个消息处理对象Handler
        mMainActivity.mInvestHandler = new Handler() {
            // 重载消息处理方法，用于接收和处理WorkerThread发送的消息
            @Override
            public void handleMessage(Message msg) {
                mMarketDatas = (String[][]) msg.obj;
                for (int i = 0; i < indexArray.length; i++) {
                    mBeanInvestList.get(i).mRealPoint = mMarketDatas[indexArray[i]][1];
                    if (weeksArray[i] > 0) {
                        double basePoint = mBeanInvestList.get(i).mStartPoint + weeksArray[i] * mBeanInvestList.get(i).mSlope;
                        mBeanInvestList.get(i).mBasePoint = Double.toString(basePoint);
                        double diffRate = Double.parseDouble(mBeanInvestList.get(i).mRealPoint) / basePoint;
                        double divisor = mBeanInvestList.get(i).mDivisor;
                        double investAmount = (basePoint / divisor) / Math.pow(diffRate, mBeanInvestList.get(i).mDiffCoef);
                        mBeanInvestList.get(i).mQuota = (diffRate <= 1) ? String.format("%.2f", investAmount) : "无需投资";
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
