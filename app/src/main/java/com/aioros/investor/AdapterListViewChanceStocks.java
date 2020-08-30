package com.aioros.investor;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by zhangxr on 2020/07/18.
 */

public class AdapterListViewChanceStocks extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    String[][] mStockDatas;

    public AdapterListViewChanceStocks(Context context, String[][] datas) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mStockDatas = datas;
    }

    @Override
    public int getCount() {
        return mStockDatas.length;
    }

    @Override
    public Object getItem(int position) {
        return mStockDatas[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = mInflater.inflate(R.layout.item_chance_stocks, null);
        String[] stockData = mStockDatas[position];
        int color = Color.rgb(128, 128, 128);
        double probs = Double.parseDouble(stockData[4]) * 100;
        double moneys = probs * 550;
        if (probs >= 90) {
            color = Color.rgb(255, 30, 30);
        } else if (probs >= 75) {
            color = Color.rgb(255, 100, 100);
        }
        View chanceStockView = (View) view.findViewById(R.id.layoutChanceStocks);
        GradientDrawable gradientDrawable = (GradientDrawable) chanceStockView.getBackground();
        gradientDrawable.setColor(color);

        TextView name = (TextView) view.findViewById(R.id.textViewChanceName);
        name.setText(stockData[0]);

        TextView code = (TextView) view.findViewById(R.id.textViewChanceCode);
        code.setText(stockData[1]);

        TextView price = (TextView) view.findViewById(R.id.textViewChancePrice);
        price.setText(stockData[2]);

        TextView ratio = (TextView) view.findViewById(R.id.textViewChanceRatio);
        ratio.setText(stockData[3] + "%");

        TextView prob = (TextView) view.findViewById(R.id.textViewChanceProb);
        prob.setText(String.format("%.1f%%", probs));

        TextView money = (TextView) view.findViewById(R.id.textViewChanceMoney);
        money.setText(String.format("¥%d", (int)moneys));

        return view;
    }
}
