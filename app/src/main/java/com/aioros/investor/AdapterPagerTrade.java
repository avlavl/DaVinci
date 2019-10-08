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
        if (fileUtility.importDataFile1("investor/data/" + mFragmentTrade.mBaseNames[position] + ".txt") == 0) {
            if (false == mFragmentTrade.mTabTitles[position].equals(mFragmentTrade.mBaseNames[position])) {
                fileUtility.importDataFile2("investor/data/" + mFragmentTrade.mTabTitles[position] + ".txt");
            }
            TradeCheck tradeCheck = new TradeCheck(fileUtility);
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
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int item, long id) {
                mListViewOnItemLongClick(position, item);
                return true;
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
        int[] idxArray = new int[]{INDEX_QZYY, INDEX_ZZJG, INDEX_SWZQ, INDEX_HSSB, INDEX_GZMT};
        BeanTradeMode tradeMode = mBeanTradeModeLists.get(position).get(item);
        TradeCheck tradeCheck = mTradeCheckList.get(position);
        ArrayList<String> dateList = (tradeCheck.rows2 == 0) ? tradeCheck.dateList : tradeCheck.dateList2;
        ArrayList<Double> closeList = (tradeCheck.rows2 == 0) ? tradeCheck.closeList : tradeCheck.closeList2;
        if (tradeMode.bpIdxList.size() > tradeMode.spIdxList.size()) {
            tradeMode.spIdxList.add(tradeCheck.rows - 1);
        }
        ArrayList<Integer> bpIdxList = tradeMode.bpIdxList;
        ArrayList<Integer> spIdxList = tradeMode.spIdxList;
        if (tradeCheck.rows2 != 0) {
            int offset = tradeCheck.rows - tradeCheck.rows2;
            bpIdxList = new ArrayList<>();
            spIdxList = new ArrayList<>();
            for (int i = 0; i < tradeMode.bpIdxList.size(); i++) {
                if (tradeMode.spIdxList.get(i) > offset) {
                    bpIdxList.add((tradeMode.bpIdxList.get(i) > offset) ? (tradeMode.bpIdxList.get(i) - offset) : 0);
                    spIdxList.add(tradeMode.spIdxList.get(i) - offset);
                }
            }
        }
        int tradeLogs = bpIdxList.size();
        String[] bpDateArray = new String[tradeLogs];
        String[] spDateArray = new String[tradeLogs];
        Integer[] daysArray = new Integer[tradeLogs];
        Double[] bpValArray = new Double[tradeLogs];
        Double[] spValArray = new Double[tradeLogs];
        Double[] yieldArray = new Double[tradeLogs];
        Double[] ratioArray = new Double[tradeLogs];
        for (int i = 0; i < tradeLogs; i++) {
            bpDateArray[i] = dateList.get(bpIdxList.get(tradeLogs - i - 1));
            bpValArray[i] = closeList.get(bpIdxList.get(tradeLogs - i - 1));
            if (tradeMode.mStatus) {
                spDateArray[i] = (i == 0) ? TimeUtility.getCurrentDate() : dateList.get(spIdxList.get(tradeLogs - 1 - i));
                spValArray[i] = (i == 0) ? Double.parseDouble(mFragmentTrade.mMarketDatas[idxArray[position]][1]) : closeList.get(spIdxList.get(tradeLogs - 1 - i));
            } else {
                spDateArray[i] = dateList.get(spIdxList.get(tradeLogs - i - 1));
                spValArray[i] = closeList.get(spIdxList.get(tradeLogs - i - 1));
            }
            daysArray[i] = TimeUtility.daysBetween(bpDateArray[i], spDateArray[i]);
            yieldArray[i] = ((bpValArray[i]) > 0 && (spValArray[i] > 0)) ? Double.parseDouble(tradeMode.mAmount.substring(0, tradeMode.mAmount.length() - 1)) * (spValArray[i] - bpValArray[i]) / bpValArray[i] : 0;
            ratioArray[i] = ((bpValArray[i]) > 0 && (spValArray[i] > 0)) ? 100 * (spValArray[i] - bpValArray[i]) / bpValArray[i] : 0;
        }

        AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.dialog_trade_record);

        List<BeanTradeRecord> mBeanTradeRecordList = new ArrayList<BeanTradeRecord>();
        for (int i = 0; i < tradeLogs; i++) {
            mBeanTradeRecordList.add(new BeanTradeRecord(bpDateArray[i], spDateArray[i], daysArray[i], bpValArray[i], spValArray[i], yieldArray[i], ratioArray[i]));
        }
        ListView listView = (ListView) window.findViewById(R.id.listViewTradeLog);
        AdapterListViewTradeRecord adapterListView = new AdapterListViewTradeRecord(mContext, mBeanTradeRecordList, position);
        listView.setAdapter(adapterListView);
    }

    private void mListViewOnItemLongClick(int position, int item) {
        BeanTradeMode tradeMode = mBeanTradeModeLists.get(position).get(item);
        TradeCheck tradeCheck = mTradeCheckList.get(position);
        ArrayList<String> dateList = (tradeCheck.rows2 == 0) ? tradeCheck.dateList : tradeCheck.dateList2;
        ArrayList<Double> closeList = (tradeCheck.rows2 == 0) ? tradeCheck.closeList : tradeCheck.closeList2;
        if (tradeMode.bpIdxList.size() > tradeMode.spIdxList.size()) {
            tradeMode.spIdxList.add(tradeCheck.rows - 1);
        }
        ArrayList<Integer> bpIdxList = tradeMode.bpIdxList;
        ArrayList<Integer> spIdxList = tradeMode.spIdxList;
        if (tradeCheck.rows2 != 0) {
            int offset = tradeCheck.rows - tradeCheck.rows2;
            bpIdxList = new ArrayList<>();
            spIdxList = new ArrayList<>();
            for (int i = 0; i < tradeMode.bpIdxList.size(); i++) {
                if (tradeMode.spIdxList.get(i) > offset) {
                    bpIdxList.add((tradeMode.bpIdxList.get(i) > offset) ? (tradeMode.bpIdxList.get(i) - offset) : 0);
                    spIdxList.add(tradeMode.spIdxList.get(i) - offset);
                }
            }
        }

        TradeHandle tradeHandle = new TradeHandle(dateList, closeList, bpIdxList, spIdxList);
        ArrayList<Double> fundList = tradeHandle.synthesize();

        AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.dialog_trade_info);

        TextView textViewCurrentAsset = (TextView) window.findViewById(R.id.textViewDialogCurrentAsset);
        textViewCurrentAsset.setText(String.format("%.2f", tradeHandle.getCurrentAsset()));
        TextView textViewInitAsset = (TextView) window.findViewById(R.id.textViewDialogInitAsset);
        textViewInitAsset.setText(String.format("%.2f", tradeHandle.getInitAsset()));
        TextView textViewNetProfit = (TextView) window.findViewById(R.id.textViewDialogNetProfit);
        textViewNetProfit.setText(String.format("%.2f", tradeHandle.getNetProfit()));
        TextView textViewObjectRate = (TextView) window.findViewById(R.id.textViewDialogObjectRate);
        textViewObjectRate.setText(String.format("%.3f%%", tradeHandle.getObjectRate()));
        TextView textViewSystemRate = (TextView) window.findViewById(R.id.textViewDialogSystemRate);
        textViewSystemRate.setText(String.format("%.3f%%", tradeHandle.getSystemRate()));
        TextView textViewSysObjRatio = (TextView) window.findViewById(R.id.textViewDialogSysObjRatio);
        textViewSysObjRatio.setText(String.format("%.3f", tradeHandle.getSysObjRatio()));
        TextView textViewAnnualRate = (TextView) window.findViewById(R.id.textViewDialogAnnualRate);
        textViewAnnualRate.setText(String.format("%.3f%%", tradeHandle.getAnnualRate()));
        TextView textViewTradeYears = (TextView) window.findViewById(R.id.textViewDialogTradeYears);
        textViewTradeYears.setText(String.format("%.3f年", tradeHandle.getTradeYears()));
        TextView textViewPositionYears = (TextView) window.findViewById(R.id.textViewDialogPositionYears);
        textViewPositionYears.setText(String.format("%.3f年", tradeHandle.getPositionYears()));
        TextView textViewPositionDaysRate = (TextView) window.findViewById(R.id.textViewDialogPositionDaysRate);
        textViewPositionDaysRate.setText(String.format("%.3f%%", tradeHandle.getPositionDaysRate()));
        TextView textViewMeanPositionDays = (TextView) window.findViewById(R.id.textViewDialogMeanPositionDays);
        textViewMeanPositionDays.setText(String.format("%.2f天", tradeHandle.getMeanPositionDays()));
        TextView textViewMeanGainDays = (TextView) window.findViewById(R.id.textViewDialogMeanGainDays);
        textViewMeanGainDays.setText(String.format("%.2f天", tradeHandle.getMeanGainDays()));
        TextView textViewMeanLossDays = (TextView) window.findViewById(R.id.textViewDialogMeanLossDays);
        textViewMeanLossDays.setText(String.format("%.2f天", tradeHandle.getMeanLossDays()));
        TextView textViewStandardAnnualRate = (TextView) window.findViewById(R.id.textViewDialogStandardAnnualRate);
        textViewStandardAnnualRate.setText(String.format("%.3f%%", tradeHandle.getStandardAnnualRate()));
        TextView textViewPositionAnnualRate = (TextView) window.findViewById(R.id.textViewDialogPositionAnnualRate);
        textViewPositionAnnualRate.setText(String.format("%.3f%%", tradeHandle.getPositionAnnualRate()));
        TextView textViewEvenEarningRate = (TextView) window.findViewById(R.id.textViewDialogEvenEarningRate);
        textViewEvenEarningRate.setText(String.format("%.3f%%", tradeHandle.getEvenEarningRate()));

        TextView textViewTradeTimes = (TextView) window.findViewById(R.id.textViewTradeTimes);
        textViewTradeTimes.setText(String.format("%d次", tradeHandle.getTradeTimes()));
        TextView textViewGainTimes = (TextView) window.findViewById(R.id.textViewGainTimes);
        textViewGainTimes.setText(String.format("%d次", tradeHandle.getGainTimes()));
        TextView textViewLossTimes = (TextView) window.findViewById(R.id.textViewLossTimes);
        textViewLossTimes.setText(String.format("%d次", tradeHandle.getLossTimes()));
        TextView textViewWinRate = (TextView) window.findViewById(R.id.textViewWinRate);
        textViewWinRate.setText(String.format("%.3f%%", tradeHandle.getWinRate()));
        TextView textViewMeanGain = (TextView) window.findViewById(R.id.textViewMeanGain);
        textViewMeanGain.setText(String.format("%.3f%%", tradeHandle.getMeanGain()));
        TextView textViewMeanLoss = (TextView) window.findViewById(R.id.textViewMeanLoss);
        textViewMeanLoss.setText(String.format("%.3f%%", tradeHandle.getMeanLoss()));
        TextView textViewOdds = (TextView) window.findViewById(R.id.textViewOdds);
        textViewOdds.setText(String.format("%.3f", tradeHandle.getOdds()));
        TextView textViewExpectation = (TextView) window.findViewById(R.id.textViewExpectation);
        textViewExpectation.setText(String.format("%.3f", tradeHandle.getExpectation()));
        TextView textViewGainProfit = (TextView) window.findViewById(R.id.textViewGainProfit);
        textViewGainProfit.setText(String.format("%.2f", tradeHandle.getGainProfit()));
        TextView textViewLossProfit = (TextView) window.findViewById(R.id.textViewLossProfit);
        textViewLossProfit.setText(String.format("%.2f", tradeHandle.getLossProfit()));
        TextView textViewMaxGain = (TextView) window.findViewById(R.id.textViewMaxGain);
        textViewMaxGain.setText(String.format("%.3f%%", tradeHandle.getMaxGain()));
        TextView textViewMaxLoss = (TextView) window.findViewById(R.id.textViewMaxLoss);
        textViewMaxLoss.setText(String.format("%.3f%%", tradeHandle.getMaxLoss()));
        TextView textViewMaxGainTimes = (TextView) window.findViewById(R.id.textViewMaxGainTimes);
        textViewMaxGainTimes.setText(String.format("%d次", tradeHandle.getMaxGainTimes()));
        TextView textViewMaxLossTimes = (TextView) window.findViewById(R.id.textViewMaxLossTimes);
        textViewMaxLossTimes.setText(String.format("%d次", tradeHandle.getMaxLossTimes()));
        TextView textViewMaxGainRatio = (TextView) window.findViewById(R.id.textViewMaxGainRatio);
        textViewMaxGainRatio.setText(String.format("%.3f%%", tradeHandle.getMaxGainRatio()));
        TextView textViewMaxLossRatio = (TextView) window.findViewById(R.id.textViewMaxLossRatio);
        textViewMaxLossRatio.setText(String.format("%.3f%%", tradeHandle.getMaxLossRatio()));
    }
}
