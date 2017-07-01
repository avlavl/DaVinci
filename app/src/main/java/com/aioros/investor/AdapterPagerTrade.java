package com.aioros.investor;

import android.content.Context;
import android.os.Environment;
import android.os.Looper;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by aizhang on 2017/6/17.
 */

public class AdapterPagerTrade extends PagerAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private String mTabTitles[] = new String[]{"沪深300", "淘金100", "腾讯济安", "养老产业", "医药100", "中证500", "创业板指"};
    private String mTabCodes[] = new String[]{"h000300", "H30537", "h000847", "z399812", "h000978", "h000905", "z399006"};
    private String mImportName[] = new String[]{"沪深300", "沪深300", "腾讯济安", "沪深300", "沪深300", "中证500", "创业板指"};
    private ListView mListView;
    private AdapterListViewTradeMode mAdapterListView;
    private List<BeanTradeMode> mBeanTradeModeList = new ArrayList<BeanTradeMode>();
    private FileUtility fileUtility = new FileUtility();


    public AdapterPagerTrade(Context context) {
        mContext = context;
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
//            ImageView imageView = new ImageView(mMainActivity);
//            imageView.setBackgroundResource(R.drawable.fund_select);
//            container.addView(imageView);
//            return imageView;
        if (fileUtility.importIniFile("investor/ini/" + mTabTitles[position] + ".txt") == 0) {
            mBeanTradeModeList = new ArrayList<>();
            for (String mode : fileUtility.modeList) {
                String[] paras = mode.split(" ");
                mBeanTradeModeList.add(new BeanTradeMode(paras[0], paras[1], paras[2]));
            }
        }

        if (fileUtility.importDataFile("investor/data/" + mImportName[position] + ".txt") == 0) {
            TradeCheck tradeCheck = new TradeCheck(fileUtility);
            for (int i = 0; i < mBeanTradeModeList.size(); i++) {
                switch (mBeanTradeModeList.get(i).mModeName) {
                    case "MA":
                        tradeCheck.sysMAChk(mBeanTradeModeList.get(i));
                        break;
                    case "LML":
                    case "LMS":
                        tradeCheck.sysLMChk(mBeanTradeModeList.get(i));
                        break;
                    case "BAR":
                    case "DIF":
                        tradeCheck.sysMACDChk(mBeanTradeModeList.get(i));
                        break;
                    case "BARDIF":
                        tradeCheck.sysMACD2Chk(mBeanTradeModeList.get(i));
                        break;
                    case "BARMA":
                    case "DIFMA":
                        tradeCheck.sysMACDMAChk(mBeanTradeModeList.get(i));
                        break;
                    case "BARLML":
                    case "BARLMS":
                    case "DIFLML":
                    case "DIFLMS":
                        tradeCheck.sysMACDLMChk(mBeanTradeModeList.get(i));
                        break;
                    default:
                        break;
                }
            }
//            for (BeanTradeMode tradeMode : mBeanTradeModeList) {
//                switch (tradeMode.mModeName) {
//                    case "MA":
//                        tradeCheck.sysMAChk(tradeMode);
//                        break;
//                    case "LML":
//                    case "LMS":
//                        tradeCheck.sysLMChk(tradeMode);
//                        break;
//                    case "BAR":
//                    case "DIF":
//                        tradeCheck.sysMACDChk(tradeMode);
//                        break;
//                    case "BARDIF":
//                        tradeCheck.sysMACD2Chk(tradeMode);
//                        break;
//                    case "BARMA":
//                    case "DIFMA":
//                        tradeCheck.sysMACDMAChk(tradeMode);
//                        break;
//                    case "BARLML":
//                    case "BARLMS":
//                    case "DIFLML":
//                    case "DIFLMS":
//                        tradeCheck.sysMACDLMChk(tradeMode);
//                        break;
//                    default:
//                        break;
//                }
//            }
        }

        View view = mInflater.inflate(R.layout.pager_trade, null);
        Button button = (Button) view.findViewById(R.id.buttonPagerTrade);
        button.setText(mTabTitles[position]);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonOnClick(view, position);
            }
        });
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

    public View getTabView(int position) {
        View view = mInflater.inflate(R.layout.item_tabs, null);
        TextView textView = (TextView) view.findViewById(R.id.textview_tabs);
        textView.setText(mTabTitles[position]);
        return view;
    }

    private void buttonOnClick(View v, int position) {
        Thread dft = new UpdateDataThread(position);
        dft.start();
    }

    class UpdateDataThread extends Thread {
        private int index;

        public UpdateDataThread(int idx) {
            index = idx;
        }

        @Override
        public void run() {
            String storageDir = Environment.getExternalStorageDirectory().toString();
            String filePath = storageDir + "/investor/data/" + mTabTitles[index] + ".txt";
            File file = new File(filePath);
            if (file.exists()) {
                try {
                    String urlStr = "http://hq.sinajs.cn/list=s" + mTabCodes[index];
                    HttpUtility httpUtility = new HttpUtility();
                    String httpStr = httpUtility.getData(urlStr);
                    if (httpStr.equals("")) {
                        Looper.prepare();
                        Toast.makeText(mContext, "网络无连接！", Toast.LENGTH_LONG).show();
                        Looper.loop();
                    } else if (httpStr.contains("pv_none_match")) {
                        Looper.prepare();
                        Toast.makeText(mContext, "找不到对应的股票！", Toast.LENGTH_LONG).show();
                        Looper.loop();
                    } else {
                        String[] strs = httpStr.substring(httpStr.indexOf("\"") + 1, httpStr.lastIndexOf("\"")).split(",");
                        String dataStr = String.format("%s\t%.2f\t%.2f\t%.2f\t%.2f", strs[30].replace("-", "/"),
                                Double.parseDouble(strs[1]), Double.parseDouble(strs[4]), Double.parseDouble(strs[5]), Double.parseDouble(strs[3]));
                        PrintWriter pw = new PrintWriter(new FileWriter(file, true));
                        pw.println(dataStr);
                        pw.close();
                        Looper.prepare();
                        Toast.makeText(mContext, "更新成功！", Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }
                } catch (IOException | NumberFormatException e) {
                    e.printStackTrace();
                }
            } else {
                Looper.prepare();
                Toast.makeText(mContext, "请先建立基础文件", Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }
    }
}
