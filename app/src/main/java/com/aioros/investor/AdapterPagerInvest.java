package com.aioros.investor;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by aizhang on 2017/6/17.
 */

public class AdapterPagerInvest extends PagerAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private String mTabTitles[] = new String[]{"申万证券", "养老产业"};
    private List<BeanInvest> mInvestBeanList = null;


    public AdapterPagerInvest(Context context, List<BeanInvest> beanList) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mInvestBeanList = beanList;
    }

    @Override
    public int getCount() {
        return mTabTitles.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabTitles[position];
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = mInflater.inflate(R.layout.pager_invest, null);

        TextView textViewInvestQuota = (TextView) view.findViewById(R.id.textViewInvestQuota);
        textViewInvestQuota.setText(String.format("%.2f", mInvestBeanList.get(2 * position).mQuota + mInvestBeanList.get(2 * position + 1).mQuota));
        TextView textViewInvestRealPoint = (TextView) view.findViewById(R.id.textViewInvestRealPoint);
        textViewInvestRealPoint.setText("" + mInvestBeanList.get(2 * position).mRealPoint);
        TextView textViewInvestBasePoint = (TextView) view.findViewById(R.id.textViewInvestBasePoint);
        textViewInvestBasePoint.setText(String.format("%d", (int) mInvestBeanList.get(2 * position).mBasePoint));
        TextView textViewInvestTotalProperty = (TextView) view.findViewById(R.id.textViewInvestTotalProperty);
        textViewInvestTotalProperty.setText(String.format("%.2f", mInvestBeanList.get(2 * position).mProperty + mInvestBeanList.get(2 * position + 1).mProperty));

        TextView textViewInvestTimes0 = (TextView) view.findViewById(R.id.textViewInvestTimes0);
        textViewInvestTimes0.setText(String.format("定投%d次", mInvestBeanList.get(2 * position).mTimes));
        TextView textViewInvestCost0 = (TextView) view.findViewById(R.id.textViewInvestCost0);
        textViewInvestCost0.setText(String.format("— %.2f —", mInvestBeanList.get(2 * position).mCurrentCost));
        TextView textViewInvestProperty0 = (TextView) view.findViewById(R.id.textViewInvestProperty0);
        textViewInvestProperty0.setText(String.format("%.2f", mInvestBeanList.get(2 * position).mProperty));
        TextView textViewInvestIncome0 = (TextView) view.findViewById(R.id.textViewInvestIncome0);
        textViewInvestIncome0.setText(String.format("%.2f", mInvestBeanList.get(2 * position).mIncome));
        textViewInvestIncome0.setTextColor(mInvestBeanList.get(2 * position).mIncome > 0 ? Color.rgb(200, 0, 0) : Color.rgb(0, 128, 0));
        TextView textViewInvestYield0 = (TextView) view.findViewById(R.id.textViewInvestYield0);
        textViewInvestYield0.setText(String.format("%.2f%%", mInvestBeanList.get(2 * position).mYield));
        textViewInvestYield0.setTextColor(mInvestBeanList.get(2 * position).mYield > 0 ? Color.rgb(200, 0, 0) : Color.rgb(0, 128, 0));
        TextView textViewInvestKeyPoint0 = (TextView) view.findViewById(R.id.textViewInvestKeyPoint0);
        textViewInvestKeyPoint0.setText(String.format("%.2f", mInvestBeanList.get(2 * position).mKeyPoint));

        TextView textViewInvestTimes1 = (TextView) view.findViewById(R.id.textViewInvestTimes1);
        textViewInvestTimes1.setText(String.format("定投%d次", mInvestBeanList.get(2 * position + 1).mTimes));
        TextView textViewInvestCost1 = (TextView) view.findViewById(R.id.textViewInvestCost1);
        textViewInvestCost1.setText(String.format("— %.2f —", mInvestBeanList.get(2 * position + 1).mCurrentCost));
        TextView textViewInvestProperty1 = (TextView) view.findViewById(R.id.textViewInvestProperty1);
        textViewInvestProperty1.setText(String.format("%.2f", mInvestBeanList.get(2 * position + 1).mProperty));
        TextView textViewInvestIncome1 = (TextView) view.findViewById(R.id.textViewInvestIncome1);
        textViewInvestIncome1.setText(String.format("%.2f", mInvestBeanList.get(2 * position + 1).mIncome));
        textViewInvestIncome1.setTextColor(mInvestBeanList.get(2 * position + 1).mIncome > 0 ? Color.rgb(200, 0, 0) : Color.rgb(0, 128, 0));
        TextView textViewInvestYield1 = (TextView) view.findViewById(R.id.textViewInvestYield1);
        textViewInvestYield1.setText(String.format("%.2f%%", mInvestBeanList.get(2 * position + 1).mYield));
        textViewInvestYield1.setTextColor(mInvestBeanList.get(2 * position + 1).mYield > 0 ? Color.rgb(200, 0, 0) : Color.rgb(0, 128, 0));
        TextView textViewInvestKeyPoint1 = (TextView) view.findViewById(R.id.textViewInvestKeyPoint1);
        textViewInvestKeyPoint1.setText(String.format("%.2f", mInvestBeanList.get(2 * position + 1).mKeyPoint));

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public View getTabView(int position) {
        View view = mInflater.inflate(R.layout.item_tabs, null);
        TextView textView = (TextView) view.findViewById(R.id.textview_tabs);
        textView.setText(mTabTitles[position]);
        return view;
    }
}
