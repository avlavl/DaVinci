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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate------");
        mMainActivity = (MainActivity) getActivity();
        mBeanInvestList.add(new BeanInvest(1000, 7.5, 20, 1.5, 30));
        mBeanInvestList.add(new BeanInvest(1000, 7, 20, 1.5, 20));
        mBeanInvestList.add(new BeanInvest(1400, 10, 20, 1.5, 20));
        fileUtility.importDataFile("investor/data/W申万证券.txt");

        // 在主线程中声明一个消息处理对象Handler
        mMainActivity.mInvestHandler = new Handler() {
            // 重载消息处理方法，用于接收和处理WorkerThread发送的消息
            @Override
            public void handleMessage(Message msg) {
                String[] message = (String[]) msg.obj;
                String[] strs = message[1].substring(message[1].indexOf("\"") + 1, message[1].lastIndexOf("\"")).split("~");
                mBeanInvestList.get(0).setmRealPoint(strs[3]);
                strs = message[8].substring(message[8].indexOf("\"") + 1, message[8].lastIndexOf("\"")).split("~");
                mBeanInvestList.get(1).setmRealPoint(strs[3]);
                strs = message[6].substring(message[6].indexOf("\"") + 1, message[6].lastIndexOf("\"")).split("~");
                mBeanInvestList.get(2).setmRealPoint(strs[3]);

                double basePoint = mBeanInvestList.get(1).getmStartPoint() + fileUtility.rows * mBeanInvestList.get(1).getmSlope();
                mBeanInvestList.get(1).setmBasePoint(Double.toString(basePoint));

                double diffRate = Double.parseDouble(mBeanInvestList.get(1).getmRealPoint()) / basePoint;
                double divisor = mBeanInvestList.get(1).getmDivisor();
                double input = (basePoint / divisor) / Math.pow(diffRate, mBeanInvestList.get(1).getmDiffCoef());
                if (diffRate <= 1) {
                    mBeanInvestList.get(1).setmQuota(String.format("%.2f", input));
                } else {
                    mBeanInvestList.get(1).setmQuota("无需投资");
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
