package com.aioros.investor;

import android.content.Context;
import android.graphics.Color;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import static com.aioros.investor.Constant.*;

/**
 * Created by zhangxr on 2018/11/13.
 */

public class AdapterListViewTradeRecord extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<BeanTradeRecord> mTradeLogBeanList = null;
    private int mPosition;

    public AdapterListViewTradeRecord(Context context, List<BeanTradeRecord> beanList, int pos) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mTradeLogBeanList = beanList;
        mPosition = pos;
    }

    @Override
    public int getCount() {
        return mTradeLogBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return mTradeLogBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = mInflater.inflate(R.layout.item_trade_log, null);
        BeanTradeRecord beanTradeRecord = mTradeLogBeanList.get(position);
        int color = (beanTradeRecord.mYield > 0) ? Color.rgb(240, 0, 0) : Color.rgb(0, 128, 0);

        View dateView = (View) view.findViewById(R.id.layoutTradeLogDate);
        dateView.setBackgroundColor(color);

        TextView bpDate = (TextView) view.findViewById(R.id.textViewTradeLogBpDate);
        bpDate.setText(beanTradeRecord.mBpDate);

        TextView spDate = (TextView) view.findViewById(R.id.textViewTradeLogSpDate);
        spDate.setText(beanTradeRecord.mSpDate);

        TextView days = (TextView) view.findViewById(R.id.textViewTradeLogDays);
        days.setText(String.format("%d天", beanTradeRecord.mDays));

        TextView bp = (TextView) view.findViewById(R.id.textViewTradeLogBp);
        bp.setText(String.format((mPosition == INDEX_TRADE_ZGHL) ? "%.3f" : "%.2f", beanTradeRecord.mBpVal));

        TextView sp = (TextView) view.findViewById(R.id.textViewTradeLogSp);
        sp.setText(String.format((mPosition == INDEX_TRADE_ZGHL) ? "%.3f" : "%.2f", beanTradeRecord.mSpVal));

        TextView yield = (TextView) view.findViewById(R.id.textViewTradeLogYield);
        yield.setText(String.format("%.2f", beanTradeRecord.mYield));
        yield.setTextColor(color);

        TextView ratio = (TextView) view.findViewById(R.id.textViewTradeLogRatio);
        ratio.setText(String.format("%.3f%%", beanTradeRecord.mRatio));
        ratio.setTextColor(color);

        return view;
    }
}