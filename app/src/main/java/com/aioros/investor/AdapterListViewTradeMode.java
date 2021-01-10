package com.aioros.investor;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by aizhang on 2017/6/24.
 */
public class AdapterListViewTradeMode extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<BeanTradeMode> mBeanTradeModeList = null;
    private double mPoint;
    public double mSelfPoint;

    public AdapterListViewTradeMode(Context context, List<BeanTradeMode> beanList, double point) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mBeanTradeModeList = beanList;
        mPoint = point;
        mSelfPoint = point;
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
        int colorRatio = Color.GRAY;
        double keyRatio = Math.abs(mBeanTradeModeList.get(position).mKeyRatio);
        if (keyRatio >= 10) {
            colorRatio = Color.rgb(180, 180, 180);
        } else if (keyRatio >= 5) {
            colorRatio = Color.rgb(255, 180, 180);
        } else if (keyRatio >= 1) {
            colorRatio = Color.rgb(255, 90, 90);
        } else {
            colorRatio = Color.rgb(190, 0, 0);
        }

        View view = mInflater.inflate(R.layout.item_trade, null);
        view.setBackgroundColor((mBeanTradeModeList.get(position).mStatus) ? Color.rgb(255, 240, 240) : Color.rgb(240, 255, 240));

        View flag = (View) view.findViewById(R.id.viewItemTradeFlag);
        flag.setBackgroundColor((mBeanTradeModeList.get(position).mStatus) ? Color.rgb(240, 0, 0) : Color.rgb(0, 128, 0));

        TextView ratio = (TextView) view.findViewById(R.id.textViewItemTradeKeyRatio);
        ratio.setText(String.format("%.2f%%", mBeanTradeModeList.get(position).mKeyRatio));
        ratio.setTextColor(colorRatio);

        TextView point = (TextView) view.findViewById(R.id.textViewItemTradeKeyPoint);
        point.setText(String.format("%.3f", mBeanTradeModeList.get(position).mKeyPoint));
        point.setTextColor(colorRatio);

        if (Math.abs(mBeanTradeModeList.get(position).mKeyRatio) < 10) {
            //System.out.println("RP:" + mPoint + "  KP:" + mBeanTradeModeList.get(position).mKeyPoint);
            ImageView imageViewTradeFlag = (ImageView) view.findViewById(R.id.imageViewTradeFlag);
            if ((mBeanTradeModeList.get(position).mStatus) && (mPoint <= mBeanTradeModeList.get(position).mKeyPoint)) {
                imageViewTradeFlag.setVisibility(View.VISIBLE);
                imageViewTradeFlag.setImageResource(R.drawable.icon_sale);
            } else if ((!mBeanTradeModeList.get(position).mStatus) && (mPoint >= mBeanTradeModeList.get(position).mKeyPoint)) {
                imageViewTradeFlag.setVisibility(View.VISIBLE);
                imageViewTradeFlag.setImageResource(R.drawable.icon_buy);
            } else {
                imageViewTradeFlag.setVisibility(View.INVISIBLE);
            }
        }

        TextView name = (TextView) view.findViewById(R.id.textViewItemTradeModeName);
        name.setText(mBeanTradeModeList.get(position).mModeName);

        TextView para = (TextView) view.findViewById(R.id.textViewItemTradeModePara);
        para.setText(mBeanTradeModeList.get(position).mModePara);

        TextView gain = (TextView) view.findViewById(R.id.textViewItemTradeGain);
        double g = 100 * (mSelfPoint - mBeanTradeModeList.get(position).mBSPoint) / mBeanTradeModeList.get(position).mBSPoint;
        gain.setText(String.format("%.2f%%", g));
        gain.setTextColor((g >= 0) ? Color.rgb(240, 0, 0) : Color.rgb(0, 128, 0));

        TextView posRate = (TextView) view.findViewById(R.id.textViewItemTradePosRate);
        posRate.setText(String.format("%.2f%%", mBeanTradeModeList.get(position).mPosRate));

        TextView meanDate = (TextView) view.findViewById(R.id.textViewItemTradeMeanDate);
        meanDate.setText(String.format("%.1f天", mBeanTradeModeList.get(position).mMeanDate));

        TextView duration = (TextView) view.findViewById(R.id.textViewItemTradeDuration);
        duration.setText(mBeanTradeModeList.get(position).mDuration + "天");
        duration.setTextColor((mBeanTradeModeList.get(position).mStatus) ? Color.rgb(240, 0, 0) : Color.rgb(0, 128, 0));

        TextView amount = (TextView) view.findViewById(R.id.textViewItemTradeAmount);
        amount.setText(mBeanTradeModeList.get(position).mAmount);

        return view;
    }
}

