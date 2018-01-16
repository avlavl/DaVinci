package com.aioros.investor;

import android.content.Context;
import android.graphics.Color;
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

import static com.aioros.investor.Constant.STOCK_INI_ARRAY;

/**
 * Created by aizhang on 2017/6/17.
 */

public class AdapterPagerTrade extends PagerAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private String mTabTitles[] = new String[]{"淘金100", "养老产业", "医药100", "中国互联", "沪深300", "中证500", "创业板指"};
    private String mBaseNames[] = new String[]{"沪深300", "沪深300", "沪深300", "中国互联", "沪深300", "中证500", "创业板指"};
    private ListView mListView;
    private AdapterListViewTradeMode mAdapterListView;
    private ArrayList<ArrayList<BeanTradeMode>> mBeanTradeModeLists = new ArrayList<>();
    private FragmentTrade fragmentTrade;
    private ArrayList<TradeCheck> mTradeCheckList = new ArrayList<>();

    public AdapterPagerTrade(Context context, FragmentTrade ft) {
        mContext = context;
        fragmentTrade = ft;
        mInflater = LayoutInflater.from(context);
        for (int i = 0; i < STOCK_INI_ARRAY.length; i++) {
            ArrayList<BeanTradeMode> mBeanTradeModeList = new ArrayList<>();
            for (String mode : STOCK_INI_ARRAY[i]) {
                String[] paras = mode.split(" ");
                mBeanTradeModeList.add(new BeanTradeMode(paras[0], paras[1], paras[2]));
            }
            mBeanTradeModeLists.add(mBeanTradeModeList);
            mTradeCheckList.add(new TradeCheck(new FileUtility()));
        }
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
        // Generate title based on item position
        //第一次的代码
        return mTabTitles[position];
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
        if (fileUtility.importDataFile("investor/data/" + mBaseNames[position] + ".txt") == 0) {
            if (false == mTabTitles[position].equals(mBaseNames[position])) {
                fileUtility.importDataFile2("investor/data/" + mTabTitles[position] + ".txt");
            }
            TradeCheck tradeCheck = new TradeCheck(fileUtility);
            if (position == 3) {
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
                if (position == 3) {
                    tradeMode.mKeyPoint /= 1000;
                }
            }
        }

        View view = mInflater.inflate(R.layout.pager_trade, null);
        mListView = (ListView) view.findViewById(R.id.listViewPagerTrade);
        mAdapterListView = new AdapterListViewTradeMode(mContext, mBeanTradeModeLists.get(position));
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
        textView.setText(mTabTitles[position]);
        return view;
    }

    private void mListViewOnItemClick(int position, int item) {
        int RECORD_NUM = 3;
        int[] idxArray = new int[]{2, 5, 6, 9, 2, 3, 4};
        TradeCheck tradeCheck = mTradeCheckList.get(position);
        BeanTradeMode tradeMode = mBeanTradeModeLists.get(position).get(item);
        String[] bpDateArray = new String[RECORD_NUM];
        String[] spDateArray = new String[RECORD_NUM];
        Double[] bpArray = new Double[RECORD_NUM];
        Double[] spArray = new Double[RECORD_NUM];
        Double[] yieldArray = new Double[RECORD_NUM];
        Double[] ratioArray = new Double[RECORD_NUM];
        for (int i = 0; i < RECORD_NUM; i++) {
            bpDateArray[i] = (tradeMode.bpIdxList.size() > i) ? tradeCheck.dateList.get(tradeMode.bpIdxList.get(tradeMode.bpIdxList.size() - i - 1)) : "None";
            bpArray[i] = (tradeMode.bpIdxList.size() > i) ? tradeCheck.priceList.get(tradeMode.bpIdxList.get(tradeMode.bpIdxList.size() - i - 1)) : 0;
            if (tradeMode.mStatus) {
                spDateArray[i] = (i == 0) ? TimeUtility.getCurrentDate() : (tradeMode.spIdxList.size() > (i - 1)) ? tradeCheck.dateList.get(tradeMode.spIdxList.get(tradeMode.spIdxList.size() - i)) : "None";
                spArray[i] = (i == 0) ? Double.parseDouble(fragmentTrade.mMarketDatas[idxArray[position]][1]) : (tradeMode.spIdxList.size() > (i - 1)) ? tradeCheck.priceList.get(tradeMode.spIdxList.get(tradeMode.spIdxList.size() - i)) : 0;
            } else {
                spDateArray[i] = (tradeMode.spIdxList.size() > i) ? tradeCheck.dateList.get(tradeMode.spIdxList.get(tradeMode.spIdxList.size() - i - 1)) : "None";
                spArray[i] = (tradeMode.spIdxList.size() > i) ? tradeCheck.priceList.get(tradeMode.spIdxList.get(tradeMode.spIdxList.size() - i - 1)) : 0;
            }
            yieldArray[i] = ((bpArray[i]) > 0 && (spArray[i] > 0)) ? Double.parseDouble(tradeMode.mAmount.substring(0, tradeMode.mAmount.length() - 1)) * (spArray[i] - bpArray[i]) / bpArray[i] : 0;
            ratioArray[i] = ((bpArray[i]) > 0 && (spArray[i] > 0)) ? 100 * (spArray[i] - bpArray[i]) / bpArray[i] : 0;
        }

        AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.dialog_record);

        TextView[] textViewDateArray = {
                (TextView) window.findViewById(R.id.textViewDialogDate0), (TextView) window.findViewById(R.id.textViewDialogDate1), (TextView) window.findViewById(R.id.textViewDialogDate2)
        };
        TextView[] textViewBpArray = {
                (TextView) window.findViewById(R.id.textViewDialogBp0), (TextView) window.findViewById(R.id.textViewDialogBp1), (TextView) window.findViewById(R.id.textViewDialogBp2)
        };
        TextView[] textViewSpArray = {
                (TextView) window.findViewById(R.id.textViewDialogSp0), (TextView) window.findViewById(R.id.textViewDialogSp1), (TextView) window.findViewById(R.id.textViewDialogSp2)
        };
        TextView[] textViewYieldArray = {
                (TextView) window.findViewById(R.id.textViewDialogYield0), (TextView) window.findViewById(R.id.textViewDialogYield1), (TextView) window.findViewById(R.id.textViewDialogYield2)
        };
        TextView[] textViewRatioArray = {
                (TextView) window.findViewById(R.id.textViewDialogRatio0), (TextView) window.findViewById(R.id.textViewDialogRatio1), (TextView) window.findViewById(R.id.textViewDialogRatio2)
        };
        for (int i = 0; i < RECORD_NUM; i++) {
            textViewDateArray[i].setText(bpDateArray[i] + " -- " + spDateArray[i]);
            textViewDateArray[i].setBackgroundColor((spArray[i] > bpArray[i]) ? Color.rgb(240, 0, 0) : Color.rgb(0, 128, 0));
            textViewBpArray[i].setText(String.format((position == 3) ? "%.3f" : "%.2f", bpArray[i]));
            textViewSpArray[i].setText(String.format((position == 3) ? "%.3f" : "%.2f", spArray[i]));
            textViewYieldArray[i].setText(String.format("%.2f", yieldArray[i]));
            textViewYieldArray[i].setTextColor((yieldArray[i] > 0) ? Color.rgb(240, 0, 0) : Color.rgb(0, 128, 0));
            textViewRatioArray[i].setText(String.format("%.3f%%", ratioArray[i]));
            textViewRatioArray[i].setTextColor((yieldArray[i] > 0) ? Color.rgb(240, 0, 0) : Color.rgb(0, 128, 0));
        }
    }
}
