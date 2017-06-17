package com.aioros.investor;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * Created by aizhang on 2017/6/17.
 */

public class TradePagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 7;
    private String tabTitles[] = new String[]{"沪深300", "淘金100", "腾讯济安", "养老产业", "医药100", "中证500", "创业板指"};
    private Context context;


    public TradePagerAdapter(FragmentManager fm, Context ctx) {
        super(fm);
        context = ctx;
    }

    @Override
    public Fragment getItem(int position) {
        return PageFragment.newInstance(position + 1);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        //第一次的代码
        return tabTitles[position];
        //第二次的代码
//        Drawable image = ResourcesCompat.getDrawable(context.getResources(), imageResId[position], null);
//        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
//        SpannableString sb = new SpannableString(" ");
//        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
//        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        return sb;
        //return null;
    }

    public View getTabView(int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tabs, null);
        TextView textView = (TextView) view.findViewById(R.id.textView_tabs);
        textView.setText(tabTitles[position]);
        return view;
    }
}

