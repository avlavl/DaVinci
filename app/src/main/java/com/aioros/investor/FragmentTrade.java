package com.aioros.investor;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentTrade extends BaseFragment {

    private static final String TAG = "FragmentTrade";
    private MainActivity mMainActivity;
    private AdapterPagerTrade mAdapterPager;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layoutTrade = inflater.inflate(R.layout.fragment_trade, container, false);
        Log.d(TAG, "onCreateView---->");
        mMainActivity = (MainActivity) getActivity();
        mFragmentManager = getActivity().getFragmentManager();
        mAdapterPager = new AdapterPagerTrade(mMainActivity.getSupportFragmentManager(), mMainActivity);
        mViewPager = (ViewPager) layoutTrade.findViewById(R.id.viewpager_trade);
        mViewPager.setAdapter(mAdapterPager);
        mTabLayout = (TabLayout) layoutTrade.findViewById(R.id.tablayout_trade);
        mTabLayout.setupWithViewPager(mViewPager);
        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            //tab.setText(mAdapterPager.getPageTitle(i));
            tab.setCustomView(mAdapterPager.getTabView(i));
            if (tab.getCustomView() != null) {
                View tabView = (View) tab.getCustomView().getParent();
                tabView.setTag(i);
                tabView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = (int) v.getTag();
                        Toast.makeText(mMainActivity, "您还没有登录:" + pos, Toast.LENGTH_SHORT).show();
                        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
                            TabLayout.Tab tab = mTabLayout.getTabAt(i);
                            View tabView = (View) tab.getCustomView().getParent();
                            TextView tv = (TextView) tabView.findViewById(R.id.textview_tabs);
                            if (pos == i) {
                                tv.setTextColor(Color.RED);
                                tab.select();
                            } else {
                                tv.setTextColor(Color.WHITE);
                            }
                        }
                    }
                });
            }
        }
        return layoutTrade;
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.currFragTag = Constant.FRAGMENT_FLAG_TRADE;
    }
}
