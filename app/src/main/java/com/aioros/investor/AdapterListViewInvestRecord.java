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
 * Created by zhangxr on 2018/11/18.
 */

public class AdapterListViewInvestRecord extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<BeanInvestRecord> mInvestRecordBeanList = null;

    public AdapterListViewInvestRecord(Context context, List<BeanInvestRecord> beanList) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mInvestRecordBeanList = beanList;
    }

    @Override
    public int getCount() {
        return mInvestRecordBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return mInvestRecordBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = mInflater.inflate(R.layout.item_invest_record, null);
        BeanInvestRecord beanInvestRecord = mInvestRecordBeanList.get(position);
        int color = (beanInvestRecord.mYield > 0) ? Color.rgb(255, 100, 100) : Color.rgb(90, 180, 90);

        View dateView = (View) view.findViewById(R.id.layoutInvestRecordDate);
        GradientDrawable gradientDrawable = (GradientDrawable) dateView.getBackground();
        gradientDrawable.setColor((beanInvestRecord.mQuota == 0) ? Color.rgb(128, 128, 128) : color);

        TextView date = (TextView) view.findViewById(R.id.textViewInvestRecordDate);
        date.setText(beanInvestRecord.mDate);

        TextView price = (TextView) view.findViewById(R.id.textViewInvestRecordPrice);
        price.setText(String.format("%.2f", beanInvestRecord.mPrice));

        TextView cost = (TextView) view.findViewById(R.id.textViewInvestRecordCost);
        cost.setText(String.format("%.2f", beanInvestRecord.mCost));

        TextView quota = (TextView) view.findViewById(R.id.textViewInvestRecordQuota);
        quota.setText(String.format("%.2f", beanInvestRecord.mQuota));

        TextView amount = (TextView) view.findViewById(R.id.textViewInvestRecordAmount);
        amount.setText(String.format("%.2f", beanInvestRecord.mAmount));

        TextView yield = (TextView) view.findViewById(R.id.textViewInvestRecordYield);
        yield.setText(String.format("%.2f", beanInvestRecord.mYield));
        yield.setTextColor(color);

        TextView ratio = (TextView) view.findViewById(R.id.textViewInvestRecordRatio);
        ratio.setText(String.format("%.3f%%", beanInvestRecord.mRatio));
        ratio.setTextColor(color);

        return view;
    }
}
