package com.aioros.investor;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by aizhang on 2017/6/17.
 */

public class AdapterPagerInvest extends PagerAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private String mTabTitles[] = new String[]{"上证指数", "深证成指", "申万证券", "养老产业", "医药100"};


    public AdapterPagerInvest(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
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
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mInflater.inflate(R.layout.pager_invest, null);
        Button button = (Button) view.findViewById(R.id.button_pagerinvest);
        button.setText(mTabTitles[position]);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public View getTabView(int position) {
        View view = mInflater.inflate(R.layout.item_tabs, null);
        TextView textView = (TextView) view.findViewById(R.id.textview_tabs);
        textView.setText(mTabTitles[position]);
        return view;
    }
}
