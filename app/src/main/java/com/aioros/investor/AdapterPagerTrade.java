package com.aioros.investor;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private String mTabTitles[] = new String[]{"淘金100", "腾讯济安", "养老产业", "医药100", "沪深300", "中证500", "创业板指"};
    private String mBaseNames[] = new String[]{"沪深300", "腾讯济安", "沪深300", "沪深300", "沪深300", "中证500", "创业板指"};
    private ListView mListView;
    private AdapterListViewTradeMode mAdapterListView;
    private ArrayList<ArrayList<BeanTradeMode>> mBeanTradeModeLists = new ArrayList<>();
    private FragmentTrade fragmentTrade;

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
        updateTextView(view, position);
        mListView = (ListView) view.findViewById(R.id.listViewPagerTrade);
        mAdapterListView = new AdapterListViewTradeMode(mContext, mBeanTradeModeLists.get(position));
        mListView.setAdapter(mAdapterListView);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int item, long id) {
                //mListViewOnItemClick(parent, view, item, id);
                BeanTradeMode btm = mBeanTradeModeLists.get(position).get(item);
                String message = String.format((btm.mStatus ? "买入价：" : "卖出价：") + "%.2f    涨跌幅：%.2f%%", btm.mCost, btm.mRatio);

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setIcon(R.drawable.trade_select);
                builder.setTitle("交易信息");
                builder.setMessage(message);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Toast.makeText(mContext, "PositiveButton", Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
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
        int[] idxBase = new int[]{3, 5, 3, 3, 3, 4, 2};
        int[] idxSelf = new int[]{0, 0, 6, 7, 0, 0, 0};
        String[] marketDatas = fragmentTrade.mMarketDatas;
        String marketData = marketDatas[idxBase[position]];
        String[] datas = marketData.substring(marketData.indexOf("\"") + 1, marketData.lastIndexOf("\"")).split("~");
        textViewBasep.setText(mBaseNames[position] + ": ");
        textViewBase.setText(datas[5] + "% " + datas[3]);
        textViewBase.setTextColor((Double.parseDouble(datas[5]) > 0) ? Color.RED : Color.rgb(0, 200, 0));

        if (idxSelf[position] != 0) {
            marketData = marketDatas[idxSelf[position]];
            datas = marketData.substring(marketData.indexOf("\"") + 1, marketData.lastIndexOf("\"")).split("~");
            textViewSelfp.setText(mTabTitles[position] + ": ");
            textViewSelf.setText(datas[5] + "% " + datas[3]);
            textViewSelf.setTextColor((Double.parseDouble(datas[5]) > 0) ? Color.RED : Color.rgb(0, 200, 0));
        }
    }
}
