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
    public String[] mMarketDatas;
    private String mTabTitles[] = new String[]{"深证成指", "申万证券", "养老产业"};
    private int[] indexArray = new int[]{1, 8, 6};
    private int[] weeksArray = new int[3];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate------");
        mMainActivity = (MainActivity) getActivity();
        mMarketDatas = mMainActivity.mMarketDatas;
        mBeanInvestList.add(new BeanInvest(1000, 7.5, 20, 1.5, 30));
        mBeanInvestList.add(new BeanInvest(1000, 7, 20, 1.5, 20));
        mBeanInvestList.add(new BeanInvest(1400, 10, 20, 1.5, 20));

        for (int i = 0; i < indexArray.length; i++) {
            String marketData = mMarketDatas[indexArray[i]];
            String[] datas = marketData.substring(marketData.indexOf("\"") + 1, marketData.lastIndexOf("\"")).split("~");
            mBeanInvestList.get(i).mRealPoint = datas[3];
            fileUtility.importDataFile("investor/data/W" + mTabTitles[i] + ".txt");
            weeksArray[i] = fileUtility.rows;
            if (weeksArray[i] > 0) {
                double basePoint = mBeanInvestList.get(i).mStartPoint + weeksArray[i] * mBeanInvestList.get(i).mSlope;
                mBeanInvestList.get(i).mBasePoint = Double.toString(basePoint);
                double diffRate = Double.parseDouble(mBeanInvestList.get(1).mRealPoint) / basePoint;
                double divisor = mBeanInvestList.get(1).mDivisor;
                double investAmount = (basePoint / divisor) / Math.pow(diffRate, mBeanInvestList.get(1).mDiffCoef);
                mBeanInvestList.get(1).mQuota = (diffRate <= 1) ? String.format("%.2f", investAmount) : "无需投资";
            }
        }

        // 在主线程中声明一个消息处理对象Handler
        mMainActivity.mInvestHandler = new Handler() {
            // 重载消息处理方法，用于接收和处理WorkerThread发送的消息
            @Override
            public void handleMessage(Message msg) {
                mMarketDatas = (String[]) msg.obj;
                for (int i = 0; i < indexArray.length; i++) {
                    String marketData = mMarketDatas[indexArray[i]];
                    String[] datas = marketData.substring(marketData.indexOf("\"") + 1, marketData.lastIndexOf("\"")).split("~");
                    mBeanInvestList.get(i).mRealPoint = datas[3];
                    if (weeksArray[i] > 0) {
                        double basePoint = mBeanInvestList.get(i).mStartPoint + weeksArray[i] * mBeanInvestList.get(i).mSlope;
                        mBeanInvestList.get(i).mBasePoint = Double.toString(basePoint);
                        double diffRate = Double.parseDouble(mBeanInvestList.get(1).mRealPoint) / basePoint;
                        double divisor = mBeanInvestList.get(1).mDivisor;
                        double investAmount = (basePoint / divisor) / Math.pow(diffRate, mBeanInvestList.get(1).mDiffCoef);
                        mBeanInvestList.get(1).mQuota = (diffRate <= 1) ? String.format("%.2f", investAmount) : "无需投资";
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
