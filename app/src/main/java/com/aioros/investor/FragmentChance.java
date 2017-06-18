package com.aioros.investor;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class FragmentChance extends BaseFragment {

    private static final String TAG = "FragmentChance";
    private MainActivity mMainActivity;
    private ChancePagerAdater pagerAdapter;
    public ViewPager viewPager;
    public TabLayout tabLayout;
    private String POSITION;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layoutChance = inflater.inflate(R.layout.fragment_chance, container, false);
        Log.d(TAG, "onCreateView---->");
        mMainActivity = (MainActivity) getActivity();
        mFragmentManager = getActivity().getFragmentManager();
        //pagerAdapter = new ChancePagerAdapter(mMainActivity.getSupportFragmentManager(), mMainActivity);
        pagerAdapter = new ChancePagerAdater();
        viewPager = (ViewPager) layoutChance.findViewById(R.id.viewpager_chance);
        viewPager.setAdapter(pagerAdapter);
        tabLayout = (TabLayout) layoutChance.findViewById(R.id.tablayout_chance);
        tabLayout.setupWithViewPager(viewPager);
        return layoutChance;
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.currFragTag = Constant.FRAGMENT_FLAG_CHANCE;
    }

    class ChancePagerAdater extends PagerAdapter {
        final int PAGE_COUNT = 4;
        private String tabTitles[] = new String[]{"银华日利", "华宝添益", "R-001", "R-0012"};

        @Override
        public int getCount() {
            return tabTitles.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            return tabTitles[position];
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
//            ImageView imageView = new ImageView(mMainActivity);
//            imageView.setBackgroundResource(R.drawable.fund_select);
//            container.addView(imageView);
//            return imageView;

            View layoutChance = LayoutInflater.from(mMainActivity).inflate(R.layout.pager_chance, null);
            EditText editText = (EditText) layoutChance.findViewById(R.id.editText);
            editText.setText(tabTitles[position]);
            container.addView(layoutChance);
            return layoutChance;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
