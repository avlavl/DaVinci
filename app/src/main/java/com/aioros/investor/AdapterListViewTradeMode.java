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
 * Created by aizhang on 2017/6/24.
 */
public class AdapterListViewTradeMode extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<BeanTradeMode> mBeanTradeModeList = null;


    public AdapterListViewTradeMode(Context context, List<BeanTradeMode> beanList) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mBeanTradeModeList = beanList;
    }

    @Override
    public int getCount() {
        return mBeanTradeModeList.size();
    }

    @Override
    public Object getItem(int position) {
        return mBeanTradeModeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int colorFlag = Color.GRAY;
        int colorDuration = Color.GRAY;
        if (mBeanTradeModeList.get(position).mStatus) {
            colorFlag = Color.rgb(240, 0, 0);
            colorDuration = Color.rgb(0x30, 0x3F, 0x9F);
        } else {
            colorFlag = Color.rgb(0, 128, 0);
        }
        View view = mInflater.inflate(R.layout.item_trade, null);

        TextView flag = (TextView) view.findViewById(R.id.textViewItemTradeFlag);
        flag.setBackgroundColor(colorFlag);


        TextView ratio = (TextView) view.findViewById(R.id.textViewItemTradeKeyRatio);
        ratio.setText(String.format("%.2f%%", mBeanTradeModeList.get(position).mKeyRatio));

        TextView point = (TextView) view.findViewById(R.id.textViewItemTradeKeyPoint);
        point.setText(String.format("%.3f", mBeanTradeModeList.get(position).mKeyPoint));

        TextView duration = (TextView) view.findViewById(R.id.textViewItemTradeDuration);
        duration.setText(mBeanTradeModeList.get(position).mDuration + "天");
        duration.setTextColor(colorDuration);

        TextView amount = (TextView) view.findViewById(R.id.textViewItemTradeAmount);
        amount.setText(mBeanTradeModeList.get(position).mAmount);

        TextView name = (TextView) view.findViewById(R.id.textViewItemTradeModeName);
        name.setText(mBeanTradeModeList.get(position).mModeName);

        TextView para = (TextView) view.findViewById(R.id.textViewItemTradeModePara);
        para.setText(mBeanTradeModeList.get(position).mModePara);
        return view;
    }
}

