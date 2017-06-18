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
    private AdapterPagerChance mAdapterPager;
    public ViewPager mViewPager;
    public TabLayout mTabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chance, container, false);
        Log.d(TAG, "onCreateView---->");
        mMainActivity = (MainActivity) getActivity();
        mFragmentManager = getActivity().getFragmentManager();
        mAdapterPager = new AdapterPagerChance(mMainActivity);
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager_chance);
        mViewPager.setAdapter(mAdapterPager);
        mTabLayout = (TabLayout) view.findViewById(R.id.tablayout_chance);
        mTabLayout.setupWithViewPager(mViewPager);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.currFragTag = Constant.FRAGMENT_FLAG_CHANCE;
    }
}
