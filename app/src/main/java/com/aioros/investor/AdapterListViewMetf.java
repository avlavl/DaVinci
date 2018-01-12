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

public class AdapterListViewMetf extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<BeanStock> mStockBeanList = null;


    public AdapterListViewMetf(Context context, List<BeanStock> beanList) {
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
            color = Color.HSVToColor(new float[]{300.f, 5 * (Float.parseFloat(mStockBeanList.get(position).mStockValue) - 100), 0.5f});
        } else if (Double.parseDouble(mStockBeanList.get(position).mStockValue) < 100) {
            color = Color.HSVToColor(new float[]{180.f, 5 * (Float.parseFloat(mStockBeanList.get(position).mStockValue) - 100), 0.5f});
        }
        View view = mInflater.inflate(R.layout.item_metf, null);

        TextView name = (TextView) view.findViewById(R.id.textViewMetfName);
        name.setText(mStockBeanList.get(position).mStockName);

        TextView code = (TextView) view.findViewById(R.id.textViewMetfCode);
        code.setText(mStockBeanList.get(position).mStockCode);

        TextView value = (TextView) view.findViewById(R.id.textViewMetfValue);
        value.setText(mStockBeanList.get(position).mStockValue);
        value.setTextColor(color);

        TextView rate = (TextView) view.findViewById(R.id.textViewDailyRate);
        rate.setText(mStockBeanList.get(position).getDailyRate());
        rate.setTextColor(color);

        TextView ratio = (TextView) view.findViewById(R.id.textViewAnualRatio);
        ratio.setText(mStockBeanList.get(position).getAnualRatio());
        ratio.setBackgroundColor(color);

        return view;
    }
}
