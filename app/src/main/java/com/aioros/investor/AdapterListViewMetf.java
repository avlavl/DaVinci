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
        int colorF = Color.DKGRAY;
        int colorB = Color.DKGRAY;
        if (mStockBeanList.get(position).mStockValue.contains("--")) {
            colorF = Color.DKGRAY;
            colorB = Color.DKGRAY;
        } else if (Double.parseDouble(mStockBeanList.get(position).mStockScope) > 0) {
            colorF = Color.rgb(240, 0, 0);
            colorB = Color.rgb(200, 0, 0);
        } else if (Double.parseDouble(mStockBeanList.get(position).mStockScope) < 0) {
            colorF = Color.rgb(0, 200, 0);
            colorB = Color.rgb(0, 128, 0);
        }
        View view = mInflater.inflate(R.layout.item_metf, null);

        TextView name = (TextView) view.findViewById(R.id.textViewMetfName);
        name.setText(mStockBeanList.get(position).mStockName);

        TextView code = (TextView) view.findViewById(R.id.textViewMetfCode);
        code.setText(mStockBeanList.get(position).mStockCode);

        TextView value = (TextView) view.findViewById(R.id.textViewMetfValue);
        value.setText(mStockBeanList.get(position).mStockValue);
        value.setTextColor(colorF);

        TextView prob = (TextView) view.findViewById(R.id.textViewMetfProb);
        prob.setText(String.format("%.1f%%", Double.parseDouble(mStockBeanList.get(position).mStockProb) * 100));

        TextView ratio = (TextView) view.findViewById(R.id.textViewMetfRatio);
        ratio.setText(mStockBeanList.get(position).mStockRatio);
        ratio.setBackgroundColor(colorB);

        return view;
    }
}
