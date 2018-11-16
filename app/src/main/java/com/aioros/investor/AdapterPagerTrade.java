package com.aioros.investor;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.aioros.investor.Constant.*;

/**
 * Created by aizhang on 2017/6/17.
 */

public class AdapterPagerTrade extends PagerAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ListView mListView;
    private AdapterListViewTradeMode mAdapterListView;
    private ArrayList<ArrayList<BeanTradeMode>> mBeanTradeModeLists = new ArrayList<>();
    private FragmentTrade mFragmentTrade;
    private ArrayList<TradeCheck> mTradeCheckList = new ArrayList<>();

    public AdapterPagerTrade(Context context, FragmentTrade ft) {
        mContext = context;
        mFragmentTrade = ft;
        mInflater = LayoutInflater.from(context);
        for (int i = 0; i < STOCK_PARA_ARRAY.length; i++) {
            ArrayList<BeanTradeMode> mBeanTradeModeList = new ArrayList<>();
            for (String mode : STOCK_PARA_ARRAY[i]) {
                String[] paras = mode.split(" ");
                mBeanTradeModeList.add(new BeanTradeMode(paras[0], paras[1], paras[2]));
            }
            mBeanTradeModeLists.add(mBeanTradeModeList);
            mTradeCheckList.add(new TradeCheck(new FileUtility()));
        }
    }

    @Override
    public int getCount() {
        return mFragmentTrade.mTabTitles.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        //第一次的代码
        return mFragmentTrade.mTabTitles[position];
        //第二次的代码
//        Drawable image = ResourcesCompat.getDrawable(mContext.getResources(), imageResId[position], null);
//        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
//        SpannableString sb = new SpannableString(" ");
//        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
//        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        return sb;
        //return null;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        FileUtility fileUtility = new FileUtility();
        if (fileUtility.importDataFile("investor/data/" + mFragmentTrade.mBaseNames[position] + ".txt") == 0) {
            if (false == mFragmentTrade.mTabTitles[position].equals(mFragmentTrade.mBaseNames[position])) {
                fileUtility.importDataFile2("investor/data/" + mFragmentTrade.mTabTitles[position] + ".txt");
            }
            TradeCheck tradeCheck = new TradeCheck(fileUtility);
            if (position == INDEX_TRADE_ZGHL) {
                ArrayList<Double> zoomPriceList = new ArrayList<>();
                for (int i = 0; i < tradeCheck.rows; i++) {
                    zoomPriceList.add(tradeCheck.closeList.get(i) * 1000);
                }
                tradeCheck.closeList = zoomPriceList;
            }
            mTradeCheckList.set(position, tradeCheck);

            for (BeanTradeMode tradeMode : mBeanTradeModeLists.get(position)) {
                switch (tradeMode.mModeName) {
                    case "MA":
                        tradeCheck.sysMAChk(tradeMode);
                        break;
                    case "LML":
                    case "LMS":
                        tradeCheck.sysLMChk(tradeMode);
                        break;
                    case "BAR":
                    case "DIF":
                        tradeCheck.sysMACDChk(tradeMode);
                        break;
                    case "BARDIF":
                        tradeCheck.sysMACD2Chk(tradeMode);
                        break;
                    case "BARMA":
                    case "DIFMA":
                        tradeCheck.sysMACDMAChk(tradeMode);
                        break;
                    case "BARLML":
                    case "BARLMS":
                    case "DIFLML":
                    case "DIFLMS":
                        tradeCheck.sysMACDLMChk(tradeMode);
                        break;
                    default:
                        break;
                }
                if (position == INDEX_TRADE_ZGHL) {
                    tradeMode.mKeyPoint /= 1000;
                }
            }
        }

        View view = mInflater.inflate(R.layout.pager_trade, null);
        mListView = (ListView) view.findViewById(R.id.listViewPagerTrade);
        mAdapterListView = new AdapterListViewTradeMode(mContext, mBeanTradeModeLists.get(position), mFragmentTrade.mRealPoints[position]);
        mListView.setAdapter(mAdapterListView);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int item, long id) {
                mListViewOnItemClick(position, item);
            }

        });
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

//    @Override
//    public int getItemPosition(Object object) {
//        return POSITION_NONE;
//    }

    public View getTabView(int position) {
        View view = mInflater.inflate(R.layout.item_tabs, null);
        TextView textView = (TextView) view.findViewById(R.id.textview_tabs);
        textView.setText(mFragmentTrade.mTabTitles[position]);
        return view;
    }

    private void mListViewOnItemClick(int position, int item) {
        int[] idxArray = new int[]{INDEX_QZYY, INDEX_ZZJG, INDEX_CYBZ, INDEX_ZGHL};
        TradeCheck tradeCheck = mTradeCheckList.get(position);
        BeanTradeMode tradeMode = mBeanTradeModeLists.get(position).get(item);
        int tradeBpLogs = tradeMode.bpIdxList.size();
        int tradeSpLogs = tradeMode.spIdxList.size();
        String[] bpDateArray = new String[tradeBpLogs];
        String[] spDateArray = new String[tradeBpLogs];
        Integer[] daysArray = new Integer[tradeBpLogs];
        Double[] bpValArray = new Double[tradeBpLogs];
        Double[] spValArray = new Double[tradeBpLogs];
        Double[] yieldArray = new Double[tradeBpLogs];
        Double[] ratioArray = new Double[tradeBpLogs];
        for (int i = 0; i < tradeBpLogs; i++) {
            bpDateArray[i] = tradeCheck.dateList.get(tradeMode.bpIdxList.get(tradeBpLogs - i - 1));
            bpValArray[i] = tradeCheck.priceList.get(tradeMode.bpIdxList.get(tradeBpLogs - i - 1));
            if (tradeMode.mStatus) {
                spDateArray[i] = (i == 0) ? TimeUtility.getCurrentDate() : tradeCheck.dateList.get(tradeMode.spIdxList.get(tradeSpLogs - i));
                spValArray[i] = (i == 0) ? Double.parseDouble(mFragmentTrade.mMarketDatas[idxArray[position]][1]) : tradeCheck.priceList.get(tradeMode.spIdxList.get(tradeSpLogs - i));
            } else {
                spDateArray[i] = tradeCheck.dateList.get(tradeMode.spIdxList.get(tradeSpLogs - i - 1));
                spValArray[i] = tradeCheck.priceList.get(tradeMode.spIdxList.get(tradeSpLogs - i - 1));
            }
            daysArray[i] = TimeUtility.daysBetween(bpDateArray[i], spDateArray[i]);
            yieldArray[i] = ((bpValArray[i]) > 0 && (spValArray[i] > 0)) ? Double.parseDouble(tradeMode.mAmount.substring(0, tradeMode.mAmount.length() - 1)) * (spValArray[i] - bpValArray[i]) / bpValArray[i] : 0;
            ratioArray[i] = ((bpValArray[i]) > 0 && (spValArray[i] > 0)) ? 100 * (spValArray[i] - bpValArray[i]) / bpValArray[i] : 0;
        }

        AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.dialog_record);

        List<BeanTradeRecord> mBeanTradeRecordList = new ArrayList<BeanTradeRecord>();
        for (int i = 0; i < tradeBpLogs; i++) {
            mBeanTradeRecordList.add(new BeanTradeRecord(bpDateArray[i], spDateArray[i], daysArray[i], bpValArray[i], spValArray[i], yieldArray[i], ratioArray[i]));
        }
        ListView listView = (ListView) window.findViewById(R.id.listViewTradeLog);
        AdapterListViewTradeRecord adapterListView = new AdapterListViewTradeRecord(mContext, mBeanTradeRecordList, position);
        listView.setAdapter(adapterListView);
    }
}
