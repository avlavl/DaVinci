package com.aioros.investor;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aizhang on 2017/6/17.
 */

public class AdapterPagerInvest extends PagerAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private String mTabTitles[] = new String[]{"申万证券", "养老产业", "中证传媒"};
    private List<BeanInvest> mInvestBeanList = null;


    public AdapterPagerInvest(Context context, List<BeanInvest> beanList) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mInvestBeanList = beanList;
    }

    @Override
    public int getCount() {
        return mTabTitles.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabTitles[position];
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = mInflater.inflate(R.layout.pager_invest, null);

        TextView textViewInvestQuota = (TextView) view.findViewById(R.id.textViewTradeRecordBp);
        textViewInvestQuota.setText((mInvestBeanList.get(2 * position).mQuota != 0) ? String.format("%.2f", mInvestBeanList.get(2 * position).mQuota + mInvestBeanList.get(2 * position + 1).mQuota) : "无需定投");
        TextView textViewInvestBasePoint = (TextView) view.findViewById(R.id.textViewInvestBasePoint);
        textViewInvestBasePoint.setText(String.format("%.1f", mInvestBeanList.get(2 * position).mBasePoint));
        TextView textViewInvestDispersion = (TextView) view.findViewById(R.id.textViewInvestDispersion);
        textViewInvestDispersion.setText(String.format("%.4f", mInvestBeanList.get(2 * position).mDispersion));
        TextView textViewInvestRealPoint = (TextView) view.findViewById(R.id.textViewInvestRealPoint);
        textViewInvestRealPoint.setText("" + mInvestBeanList.get(2 * position).mRealPoint);
        TextView textViewInvestTotalProperty = (TextView) view.findViewById(R.id.textViewInvestTotalProperty);
        textViewInvestTotalProperty.setText(String.format("%.2f", mInvestBeanList.get(2 * position).mProperty + mInvestBeanList.get(2 * position + 1).mProperty));
        double totalIncome = mInvestBeanList.get(2 * position).mIncome + mInvestBeanList.get(2 * position + 1).mIncome;
        TextView textViewInvestTotalIncome = (TextView) view.findViewById(R.id.textViewInvestTotalIncome);
        textViewInvestTotalIncome.setText(String.format(((totalIncome > 0) ? "+" : "") + "%.2f", totalIncome));
        textViewInvestTotalIncome.setTextColor(totalIncome > 0 ? Color.rgb(200, 0, 0) : Color.rgb(0, 128, 0));
        double weekIncome = mInvestBeanList.get(2 * position).mWeekIncome + mInvestBeanList.get(2 * position + 1).mWeekIncome;
        TextView textViewInvestWeekIncome = (TextView) view.findViewById(R.id.textViewInvestWeekIncome);
        textViewInvestWeekIncome.setText(String.format(((weekIncome > 0) ? "+" : "") + "%.2f", weekIncome));
        textViewInvestWeekIncome.setTextColor(weekIncome > 0 ? Color.rgb(200, 0, 0) : Color.rgb(0, 128, 0));

        TextView textViewInvestTimes0 = (TextView) view.findViewById(R.id.textViewInvestTimes0);
        textViewInvestTimes0.setText(String.format("定投%d次", mInvestBeanList.get(2 * position).mTimes));
        TextView textViewInvestCost0 = (TextView) view.findViewById(R.id.textViewInvestCost0);
        textViewInvestCost0.setText(String.format("— %.2f —", mInvestBeanList.get(2 * position).mCurrentCost));
        TextView textViewInvestProperty0 = (TextView) view.findViewById(R.id.textViewInvestProperty0);
        textViewInvestProperty0.setText(String.format("%.2f", mInvestBeanList.get(2 * position).mProperty));
        TextView textViewInvestIncome0 = (TextView) view.findViewById(R.id.textViewInvestIncome0);
        textViewInvestIncome0.setText(String.format("%.2f", mInvestBeanList.get(2 * position).mIncome));
        textViewInvestIncome0.setTextColor(mInvestBeanList.get(2 * position).mIncome > 0 ? Color.rgb(200, 0, 0) : Color.rgb(0, 128, 0));
        TextView textViewInvestYield0 = (TextView) view.findViewById(R.id.textViewInvestYield0);
        textViewInvestYield0.setText(String.format("%.2f%%", mInvestBeanList.get(2 * position).mYield));
        textViewInvestYield0.setTextColor(mInvestBeanList.get(2 * position).mYield > 0 ? Color.rgb(200, 0, 0) : Color.rgb(0, 128, 0));
        TextView textViewInvestKeyPoint0 = (TextView) view.findViewById(R.id.textViewInvestKeyPoint0);
        textViewInvestKeyPoint0.setText(String.format("%.2f", mInvestBeanList.get(2 * position).mKeyPoint));

        TextView textViewInvestTimes1 = (TextView) view.findViewById(R.id.textViewInvestTimes1);
        textViewInvestTimes1.setText(String.format("定投%d次", mInvestBeanList.get(2 * position + 1).mTimes));
        TextView textViewInvestCost1 = (TextView) view.findViewById(R.id.textViewInvestCost1);
        textViewInvestCost1.setText(String.format("— %.2f —", mInvestBeanList.get(2 * position + 1).mCurrentCost));
        TextView textViewInvestProperty1 = (TextView) view.findViewById(R.id.textViewInvestProperty1);
        textViewInvestProperty1.setText(String.format("%.2f", mInvestBeanList.get(2 * position + 1).mProperty));
        TextView textViewInvestIncome1 = (TextView) view.findViewById(R.id.textViewInvestIncome1);
        textViewInvestIncome1.setText(String.format("%.2f", mInvestBeanList.get(2 * position + 1).mIncome));
        textViewInvestIncome1.setTextColor(mInvestBeanList.get(2 * position + 1).mIncome > 0 ? Color.rgb(200, 0, 0) : Color.rgb(0, 128, 0));
        TextView textViewInvestYield1 = (TextView) view.findViewById(R.id.textViewInvestYield1);
        textViewInvestYield1.setText(String.format("%.2f%%", mInvestBeanList.get(2 * position + 1).mYield));
        textViewInvestYield1.setTextColor(mInvestBeanList.get(2 * position + 1).mYield > 0 ? Color.rgb(200, 0, 0) : Color.rgb(0, 128, 0));
        TextView textViewInvestKeyPoint1 = (TextView) view.findViewById(R.id.textViewInvestKeyPoint1);
        textViewInvestKeyPoint1.setText(String.format("%.2f", mInvestBeanList.get(2 * position + 1).mKeyPoint));

        View layoutRecord0 = (View) view.findViewById(R.id.layoutInvestRecord0);
        layoutRecord0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayoutRecordOnClick(position, 0);
            }
        });

        View layoutInfo0 = (View) view.findViewById(R.id.layoutInvestInfo0);
        layoutInfo0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayoutInfoOnClick(position, 0);
            }
        });

        View layoutRecord1 = (View) view.findViewById(R.id.layoutInvestRecord1);
        layoutRecord1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayoutRecordOnClick(position, 1);
            }
        });

        View layoutInfo1 = (View) view.findViewById(R.id.layoutInvestInfo1);
        layoutInfo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayoutInfoOnClick(position, 1);
            }
        });
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public View getTabView(int position) {
        View view = mInflater.inflate(R.layout.item_tabs, null);
        TextView textView = (TextView) view.findViewById(R.id.textview_tabs);
        textView.setText(mTabTitles[position]);
        return view;
    }

    private void mLayoutRecordOnClick(int position, int item) {
        ArrayList<StrategyInvest.RecordData> recordDataList = mInvestBeanList.get(2 * position + item).mRecordDataList;
        ArrayList<StrategyInvest.RecordData> recordDataList1 = mInvestBeanList.get(2 * position).mRecordDataList;
        ArrayList<StrategyInvest.RecordData> recordDataList2 = mInvestBeanList.get(2 * position + 1).mRecordDataList;
        int investLogs = recordDataList.size();
        int investLogs1 = recordDataList1.size();
        int investLogs2 = recordDataList2.size();
        int showLogs = mInvestBeanList.get(2 * position + item).mTimes;
        String[] dataArray = new String[investLogs];
        Double[] priceArray = new Double[investLogs];
        Double[] costArray = new Double[investLogs];
        Double[] quotaArray = new Double[investLogs];
        Double[] amountArray = new Double[investLogs];
        Double[] yieldArray = new Double[investLogs];
        Double[] ratioArray = new Double[investLogs];
        for (int i = 0; i < showLogs; i++) {
            dataArray[i] = recordDataList.get(investLogs - 1 - i).date;
            priceArray[i] = recordDataList.get(investLogs - 1 - i).price;
            costArray[i] = recordDataList.get(investLogs - 1 - i).cost;
            quotaArray[i] = recordDataList1.get(investLogs1 - 1 - i).input + recordDataList2.get(investLogs2 - 1 - i).input;
            amountArray[i] = recordDataList1.get(investLogs1 - 1 - i).totalInput + recordDataList2.get(investLogs2 - 1 - i).totalInput;
            yieldArray[i] = recordDataList1.get(investLogs1 - 1 - i).profit + recordDataList2.get(investLogs2 - 1 - i).profit;
            ratioArray[i] = yieldArray[i] * 100 / amountArray[i];
        }

        AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.dialog_invest_record);

        List<BeanInvestRecord> mBeanInvestRecordList = new ArrayList<BeanInvestRecord>();
        for (int i = 0; i < showLogs; i++) {
            mBeanInvestRecordList.add(new BeanInvestRecord(dataArray[i], priceArray[i], costArray[i], quotaArray[i], amountArray[i], yieldArray[i], ratioArray[i]));
        }
        ListView listView = (ListView) window.findViewById(R.id.listViewInvestLog);
        AdapterListViewInvestRecord adapterListView = new AdapterListViewInvestRecord(mContext, mBeanInvestRecordList);
        listView.setAdapter(adapterListView);
    }

    private void mLayoutInfoOnClick(int position, int item) {
        AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.dialog_invest_info);
    }
}
