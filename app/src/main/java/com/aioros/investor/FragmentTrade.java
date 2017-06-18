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
    private TradePagerAdapter pagerAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private String POSITION;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layoutTrade = inflater.inflate(R.layout.fragment_trade, container, false);
        Log.d(TAG, "onCreateView---->");
        mMainActivity = (MainActivity) getActivity();
        mFragmentManager = getActivity().getFragmentManager();
        pagerAdapter = new TradePagerAdapter(mMainActivity.getSupportFragmentManager(), mMainActivity);
        viewPager = (ViewPager) layoutTrade.findViewById(R.id.viewpager_trade);
        viewPager.setAdapter(pagerAdapter);
        tabLayout = (TabLayout) layoutTrade.findViewById(R.id.tablayout_trade);
        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setText(pagerAdapter.getPageTitle(i));
            //tab.setCustomView(pagerAdapter.getTabView(i));
            if (tab.getCustomView() != null) {
                View tabView = (View) tab.getCustomView().getParent();
                tabView.setTag(i);
                tabView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = (int) v.getTag();
                        Toast.makeText(mMainActivity, "您还没有登录:" + pos, Toast.LENGTH_SHORT).show();
                        for (int i = 0; i < tabLayout.getTabCount(); i++) {
                            TabLayout.Tab tab = tabLayout.getTabAt(i);
                            View tabView = (View) tab.getCustomView().getParent();
                            TextView tv = (TextView) tabView.findViewById(R.id.textView_tabs);
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
