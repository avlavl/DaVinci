package com.aioros.investor;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by aizhang on 2017/6/17.
 */

public class AdapterPagerTrade extends PagerAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private String mTabTitles[] = new String[]{"沪深300", "淘金100", "腾讯济安", "养老产业", "医药100", "中证500", "创业板指"};
    private String mImportName[] = new String[]{"沪深300", "沪深300", "腾讯济安", "沪深300", "沪深300", "中证500", "创业板指"};
    private ListView mListView;
    private AdapterListViewTradeMode mAdapterListView;
    private ArrayList<BeanTradeMode> mBeanTradeModeList = new ArrayList<BeanTradeMode>();
    private FileUtility fileUtility = new FileUtility();
    private FragmentTrade fragmentTrade;

    public AdapterPagerTrade(Context context, FragmentTrade ft) {
        mContext = context;
        fragmentTrade = ft;
        mInflater = LayoutInflater.from(context);
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
        if (fileUtility.importIniFile("investor/ini/" + mTabTitles[position] + ".txt") == 0) {
            mBeanTradeModeList = new ArrayList<>();
            for (String mode : fileUtility.modeList) {
                String[] paras = mode.split(" ");
                mBeanTradeModeList.add(new BeanTradeMode(paras[0], paras[1], paras[2]));
            }
        }

        if (fileUtility.importDataFile("investor/data/" + mImportName[position] + ".txt") == 0) {
            TradeCheck tradeCheck = new TradeCheck(fileUtility);
            for (BeanTradeMode tradeMode : mBeanTradeModeList) {
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
        updateTextView(view, position);
        mListView = (ListView) view.findViewById(R.id.listViewPagerTrade);
        mAdapterListView = new AdapterListViewTradeMode(mContext, mBeanTradeModeList);
        mListView.setAdapter(mAdapterListView);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(mContext, "点击" + mBeanTradeModeList.get(position).mAmount, Toast.LENGTH_SHORT).show();
                //mListViewOnItemClick(parent, view, position, id);
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

    public void updateTextView(View view, int position) {
        TextView textViewBasep = (TextView) view.findViewById(R.id.textViewPagerTradeBasep);
        TextView textViewSelfp = (TextView) view.findViewById(R.id.textViewPagerTradeSelfp);
        TextView textViewBase = (TextView) view.findViewById(R.id.textViewPagerTradeBase);
        TextView textViewSelf = (TextView) view.findViewById(R.id.textViewPagerTradeSelf);
        int[] idxBase = new int[]{3, 3, 5, 3, 3, 4, 2};
        int[] idxSelf = new int[]{0, 0, 0, 6, 7, 0, 0};
        String[] marketDatas = fragmentTrade.mMarketDatas;
        String marketData = marketDatas[idxBase[position]];
        String[] datas = marketData.substring(marketData.indexOf("\"") + 1, marketData.lastIndexOf("\"")).split("~");
        textViewBasep.setText(mImportName[position] + ": ");
        textViewBase.setText(datas[3] + " " + datas[5] + "%");
        textViewBase.setTextColor((Double.parseDouble(datas[5]) > 0) ? Color.RED : Color.rgb(0, 200, 0));

        if (idxSelf[position] != 0) {
            marketData = marketDatas[idxSelf[position]];
            datas = marketData.substring(marketData.indexOf("\"") + 1, marketData.lastIndexOf("\"")).split("~");
            textViewSelfp.setText(mTabTitles[position] + ": ");
            textViewSelf.setText(datas[3] + " " + datas[5] + "%");
            textViewSelf.setTextColor((Double.parseDouble(datas[5]) > 0) ? Color.RED : Color.rgb(0, 200, 0));
        }
    }
}
