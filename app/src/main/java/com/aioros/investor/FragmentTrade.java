package com.aioros.investor;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentTrade extends BaseFragment {

    private static final String TAG = "FragmentTrade";
    private MainActivity mMainActivity;
    private TradePagerAdapter pagerAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private String POSITION;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layoutTrade = inflater.inflate(R.layout.trade, container, false);
        Log.d(TAG, "onCreateView---->");
        mMainActivity = (MainActivity) getActivity();
        mFragmentManager = getActivity().getFragmentManager();
        pagerAdapter = new TradePagerAdapter(mMainActivity.getSupportFragmentManager(), mMainActivity);
        viewPager = (ViewPager) layoutTrade.findViewById(R.id.viewpager);
        viewPager.setAdapter(pagerAdapter);
        tabLayout = (TabLayout) layoutTrade.findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(pagerAdapter.getTabView(i));
        }
        return layoutTrade;
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.currFragTag = Constant.FRAGMENT_FLAG_TRADE;
    }
}
