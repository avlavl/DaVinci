package com.aioros.investor;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by aizhang on 2018/1/12.
 */

public class AdapterListViewChance extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<BeanStock> mStockBeanList = null;


    public AdapterListViewChance(Context context, List<BeanStock> beanList) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mStockBeanList = beanList;
    }

    @Override
    public int getCount() {
        return mStockBeanList.size() - 1;
    }

    @Override
    public Object getItem(int position) {
        return mStockBeanList.get(position + 1);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int color = Color.DKGRAY;
        position += 1;
        if (mStockBeanList.get(position).mStockValue.contains("--")) {
            color = Color.DKGRAY;
        } else if (Double.parseDouble(mStockBeanList.get(position).mStockScope) > 0) {
            color = Color.rgb(240, 0, 0);
        } else if (Double.parseDouble(mStockBeanList.get(position).mStockScope) < 0) {
            color = Color.rgb(0, 200, 0);
        }

        float futuresGain = 200 * Float.parseFloat(mStockBeanList.get(position).mStockScope);
        float futuresDiffPoint = Float.parseFloat(mStockBeanList.get(0).mStockValue) - Float.parseFloat(mStockBeanList.get(position).mStockValue);
        float futuresDiscount = (Float.parseFloat(mStockBeanList.get(0).mStockValue) - Float.parseFloat(mStockBeanList.get(position).mStockValue)) / Float.parseFloat(mStockBeanList.get(0).mStockValue);

        View view = mInflater.inflate(R.layout.item_chance, null);

        TextView name = (TextView) view.findViewById(R.id.textViewItemFuturesName);
        name.setText(mStockBeanList.get(position).mStockName);

        TextView point = (TextView) view.findViewById(R.id.textViewItemFuturesPoint);
        point.setText(mStockBeanList.get(position).mStockValue);
        point.setTextColor(color);

        TextView ratio = (TextView) view.findViewById(R.id.textViewItemFuturesRatio);
        ratio.setText(mStockBeanList.get(position).mStockRatio);
        ratio.setTextColor(color);

        TextView scope = (TextView) view.findViewById(R.id.textViewItemFuturesScope);
        scope.setText(mStockBeanList.get(position).mStockScope);
        scope.setTextColor(color);

        TextView gain = (TextView) view.findViewById(R.id.textViewItemFuturesGain);
        gain.setText(String.format("%.0f", futuresGain));
        gain.setTextColor(color);

        TextView diffPoint = (TextView) view.findViewById(R.id.textViewItemFuturesDiffPoint);
        diffPoint.setText(String.format("%.1f", futuresDiffPoint));

        TextView meanDiffPoint = (TextView) view.findViewById(R.id.textViewItemFuturesMeanDiffPoint);
        meanDiffPoint.setText(String.format("%.3f", futuresDiffPoint / mStockBeanList.get(position).mLeftDays));

        TextView discount = (TextView) view.findViewById(R.id.textViewItemFuturesDiscount);
        discount.setText(String.format("%.2f%%", 100 * futuresDiscount));

        TextView annualDiscount = (TextView) view.findViewById(R.id.textViewItemFuturesAnnualDiscount);
        annualDiscount.setText(String.format("%.2f%%", 36500 * futuresDiscount / mStockBeanList.get(position).mLeftDays));

        TextView days = (TextView) view.findViewById(R.id.textViewItemFuturesLeftDays);
        days.setText(mStockBeanList.get(position).mLeftDays + "天");

        return view;
    }
}
