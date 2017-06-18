package com.aioros.investor;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentInvest extends BaseFragment {

    private static final String TAG = "FragmentInvest";
    private MainActivity mMainActivity;
    private InvestPagerAdapter pagerAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private String POSITION;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layoutInvest = inflater.inflate(R.layout.fragment_invest, container, false);
        Log.d(TAG, "onCreateView---->");
        mMainActivity = (MainActivity) getActivity();
        mFragmentManager = getActivity().getFragmentManager();
        pagerAdapter = new InvestPagerAdapter(mMainActivity.getSupportFragmentManager(), mMainActivity);
        viewPager = (ViewPager) layoutInvest.findViewById(R.id.viewpager_invest);
        viewPager.setAdapter(pagerAdapter);
        tabLayout = (TabLayout) layoutInvest.findViewById(R.id.tablayout_invest);
        tabLayout.setupWithViewPager(viewPager);
//        for (int i = 0; i < tabLayout.getTabCount(); i++) {
//            TabLayout.Tab tab = tabLayout.getTabAt(i);
//            tab.setText(pagerAdapter.getPageTitle(i));
//        }
        return layoutInvest;
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.currFragTag = Constant.FRAGMENT_FLAG_INVEST;
    }
}
