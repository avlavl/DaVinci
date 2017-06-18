package com.aioros.investor;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentChance extends BaseFragment {

    private static final String TAG = "FragmentChance";
    private MainActivity mMainActivity;
    private AdapterPagerChance adapterPager;
    public ViewPager viewPager;
    public TabLayout tabLayout;
    private String POSITION;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layoutChance = inflater.inflate(R.layout.fragment_chance, container, false);
        Log.d(TAG, "onCreateView---->");
        mMainActivity = (MainActivity) getActivity();
        mFragmentManager = getActivity().getFragmentManager();
        adapterPager = new AdapterPagerChance(mMainActivity);
        viewPager = (ViewPager) layoutChance.findViewById(R.id.viewpager_chance);
        viewPager.setAdapter(adapterPager);
        tabLayout = (TabLayout) layoutChance.findViewById(R.id.tablayout_chance);
        tabLayout.setupWithViewPager(viewPager);
        return layoutChance;
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.currFragTag = Constant.FRAGMENT_FLAG_CHANCE;
    }
}
