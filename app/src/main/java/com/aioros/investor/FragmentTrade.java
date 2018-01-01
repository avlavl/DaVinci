package com.aioros.investor;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import static com.aioros.investor.TimeUtility.isCheckTime;

/**
 * Created by aizhang on 2017/6/7.
 */
public class FragmentTrade extends BaseFragment {
    private static final String TAG = "FragmentTrade";
    private MainActivity mMainActivity;
    private AdapterPagerTrade mAdapterPager;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private String mTabTitles[] = new String[]{"淘金100", "养老产业", "医药100", "中国互联", "沪深300", "中证500", "创业板指"};
    private String mTabCodes[] = new String[]{"H30537", "z399812", "h000978", "z164906", "h000300", "h000905", "z399006"};
    private TextView mTextViewDate;
    private FileUtility fileUtility = new FileUtility();
    public Handler mHandler;
    private String latestDate;
    public String[][] mMarketDatas;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate------");
        mMainActivity = (MainActivity) getActivity();
        mMarketDatas = mMainActivity.mMarketDatas;

        // 在主线程中声明一个消息处理对象Handler
        mMainActivity.mTradeHandler = new Handler() {
            // 重载消息处理方法，用于接收和处理WorkerThread发送的消息
            @Override
            public void handleMessage(Message msg) {
                mMarketDatas = (String[][]) msg.obj;
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView---->");
        View view = inflater.inflate(R.layout.fragment_trade, container, false);
        mFragmentManager = mMainActivity.getFragmentManager();
        mAdapterPager = new AdapterPagerTrade(mMainActivity, this);
        mViewPager = (ViewPager) view.findViewById(R.id.viewPagerTrade);
        mViewPager.setAdapter(mAdapterPager);
        mTabLayout = (TabLayout) view.findViewById(R.id.tabLayoutTrade);
        mTabLayout.setupWithViewPager(mViewPager);

        fileUtility.importDataFile("investor/data/沪深300.txt");
        String fileDate = fileUtility.dateList.get(fileUtility.rows - 1);
        mTextViewDate = (TextView) view.findViewById(R.id.textViewTradeDate);
        mTextViewDate.setText("数据库：" + fileDate);
        checkDataUpdate(fileDate);

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String message = (String) msg.obj;
                Toast.makeText(mMainActivity, message, Toast.LENGTH_LONG).show();
                if (message.equals("更新成功！"))
                    mTextViewDate.setText("数据库：" + latestDate);
            }
        };

//        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
//            TabLayout.Tab tab = mTabLayout.getTabAt(i);
//            //tab.setText(mAdapterPager.getPageTitle(i));
//            tab.setCustomView(mAdapterPager.getTabView(i));
//            if (tab.getCustomView() != null) {
//                View tabView = (View) tab.getCustomView().getParent();
//                tabView.setTag(i);
//                tabView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        int pos = (int) v.getTag();
//                        Toast.makeText(mMainActivity, "您还没有登录:" + pos, Toast.LENGTH_SHORT).show();
//                        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
//                            TabLayout.Tab tab = mTabLayout.getTabAt(i);
//                            View tabView = (View) tab.getCustomView().getParent();
//                            TextView tv = (TextView) tabView.findViewById(R.id.textview_tabs);
//                            if (pos == i) {
//                                tv.setTextColor(Color.RED);
//                                tab.select();
//                            } else {
//                                tv.setTextColor(Color.WHITE);
//                            }
//                        }
//                    }
//                });
//            }
//        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.currFragTag = Constant.FRAGMENT_FLAG_TRADE;
    }

    private void checkDataUpdate(String date) {
        String currentDate = TimeUtility.getCurrentDate();
        if ((!currentDate.equals(date)) && (isCheckTime())) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mMainActivity);
            builder.setIcon(R.drawable.market_select);
            builder.setTitle("数据更新");
            builder.setMessage("有新的交易数据，请及时更新数据库。");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Thread udt = new UpdateDataThread();
                    udt.start();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }


    class UpdateDataThread extends Thread {
        private int index;

        public UpdateDataThread() {
        }

        public UpdateDataThread(int idx) {
            index = idx;
        }

        @Override
        public void run() {
            Message msg = mHandler.obtainMessage();
            String storageDir = Environment.getExternalStorageDirectory().toString();
            for (int i = 1; i < mTabTitles.length; i++) {
                String filePath = storageDir + "/investor/data/" + mTabTitles[i] + ".txt";
                File file = new File(filePath);
                if (file.exists()) {
                    try {
                        String urlStr = "http://hq.sinajs.cn/list=s" + mTabCodes[i];
                        HttpUtility httpUtility = new HttpUtility();
                        String httpStr = httpUtility.getData(urlStr);
                        if (httpStr.equals("")) {
                            msg.obj = "网络无连接！";
                            mHandler.sendMessage(msg);
                            return;
                        } else if (httpStr.contains("\"\"")) {
                            msg.obj = "找不到对应的股票！";
                            mHandler.sendMessage(msg);
                            return;
                        } else {
                            String[] strs = httpStr.substring(httpStr.indexOf("\"") + 1, httpStr.lastIndexOf("\"")).split(",");
                            latestDate = strs[30].replace("-", "/");
                            String dataStr = String.format((i == 4) ? "%s\t%.3f\t%.3f\t%.3f\t%.3f" : "%s\t%.2f\t%.2f\t%.2f\t%.2f", latestDate,
                                    Double.parseDouble(strs[1]), Double.parseDouble(strs[4]), Double.parseDouble(strs[5]), Double.parseDouble(strs[3]));
                            PrintWriter pw = new PrintWriter(new FileWriter(file, true));
                            pw.println(dataStr);
                            pw.close();
                        }
                    } catch (IOException | NumberFormatException e) {
                        e.printStackTrace();
                    }
                } else {
                    msg.obj = "请先建立基础文件";
                    mHandler.sendMessage(msg);
                    return;
                }
            }
            msg.obj = "更新成功！";
            mHandler.sendMessage(msg);
        }
    }
}
