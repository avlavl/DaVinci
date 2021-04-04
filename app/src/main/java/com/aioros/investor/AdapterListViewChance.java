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
        return mStockBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return mStockBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int color = Color.DKGRAY;
        if (mStockBeanList.get(position).mStockValue.contains("--")) {
            color = Color.DKGRAY;
        } else if (Double.parseDouble(mStockBeanList.get(position).mStockValue) > 100) {
            color = Color.HSVToColor(new float[]{300.f, 0.2f + 4 * (Float.parseFloat(mStockBeanList.get(position).mStockValue) - 100), 0.5f});
        } else if (Double.parseDouble(mStockBeanList.get(position).mStockValue) < 100) {
            color = Color.HSVToColor(new float[]{180.f, 0.2f + 4 * (100 - Float.parseFloat(mStockBeanList.get(position).mStockValue)), 0.5f});
        }
        View view = mInflater.inflate(R.layout.item_chance, null);

        TextView name = (TextView) view.findViewById(R.id.textViewMetfName);
        name.setText(mStockBeanList.get(position).mStockName);

        TextView value = (TextView) view.findViewById(R.id.textViewMetfValue);
        value.setText(mStockBeanList.get(position).mStockValue);
        value.setTextColor(color);

        TextView scope = (TextView) view.findViewById(R.id.textViewDailyRate);
        scope.setText(mStockBeanList.get(position).mStockScope);
        scope.setTextColor(color);

        TextView gain = (TextView) view.findViewById(R.id.textViewDailyRa);
        gain.setText(String.format("%.0f", 200 * Float.parseFloat(mStockBeanList.get(position).mStockScope)));
        gain.setTextColor(color);

        TextView ratio = (TextView) view.findViewById(R.id.textViewAnualRatio);
        ratio.setText(mStockBeanList.get(position).mStockRatio);
        ratio.setBackgroundColor(color);

        return view;
    }
}
