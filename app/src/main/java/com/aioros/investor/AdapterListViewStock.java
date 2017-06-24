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
 * Created by aizhang on 2017/6/18.
 */

public class AdapterListViewStock extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<BeanStock> mStockBeanList = null;


    public AdapterListViewStock(Context context, List<BeanStock> beanList) {
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
        if (mStockBeanList.get(position).getmStockValue().contains("--")) {
            colorF = Color.DKGRAY;
            colorB = Color.DKGRAY;
        } else if (Double.parseDouble(mStockBeanList.get(position).getmStockScope()) > 0) {
            colorF = Color.rgb(240, 0, 0);
            colorB = Color.rgb(180, 0, 30);
        } else if (Double.parseDouble(mStockBeanList.get(position).getmStockScope()) < 0) {
            colorF = Color.rgb(0, 200, 0);
            colorB = Color.rgb(0, 128, 0);
        }
        View view = mInflater.inflate(R.layout.item_stock, null);

        TextView name = (TextView) view.findViewById(R.id.textview_stockname);
        name.setText(mStockBeanList.get(position).getmStockName());

        TextView code = (TextView) view.findViewById(R.id.textview_stockcode);
        code.setText(mStockBeanList.get(position).getmStockCode());

        TextView value = (TextView) view.findViewById(R.id.textview_stockvalue);
        value.setText(mStockBeanList.get(position).getmStockValue());
        value.setTextColor(colorF);

        TextView scope = (TextView) view.findViewById(R.id.textview_stockscope);
        scope.setText(mStockBeanList.get(position).getmStockScope());
        scope.setTextColor(colorF);

        TextView ratio = (TextView) view.findViewById(R.id.textview_stockratio);
        ratio.setText(mStockBeanList.get(position).getmStockRatio());
        ratio.setBackgroundColor(colorB);

        return view;
    }
}
