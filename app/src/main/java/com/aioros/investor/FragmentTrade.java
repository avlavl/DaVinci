package com.aioros.investor;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
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
import static com.aioros.investor.Constant.*;

/**
 * Created by aizhang on 2017/6/7.
 */
public class FragmentTrade extends BaseFragment {
    private static final String TAG = "FragmentTrade";
    private MainActivity mMainActivity;
    private AdapterPagerTrade mAdapterPager;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private TextView mTextViewDate;
    private TextView mTextViewBaseName;
    private TextView mTextViewBaseData;
    private TextView mTextViewSelfName;
    private TextView mTextViewSelfData;
    private FileUtility fileUtility = new FileUtility();
    public Handler mHandler;
    private String latestDate;
    private String updateDate;
    public String[][] mMarketDatas;
    private int itemIndex = 0;
    public double[] mRealPoints = new double[NUMBER_TRADE];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate------");
        mMainActivity = (MainActivity) getActivity();
        mMarketDatas = mMainActivity.mMarketDatas;
        for (int i = 0; i < mRealPoints.length; i++) {
            mRealPoints[i] = Double.parseDouble(mMarketDatas[TRADE_IDX_BASE[i]][2]);
        }
        // 在主线程中声明一个消息处理对象Handler
        mMainActivity.mTradeHandler = new Handler() {
            // 重载消息处理方法，用于接收和处理WorkerThread发送的消息
            @Override
            public void handleMessage(Message msg) {
                mMarketDatas = (String[][]) msg.obj;
                for (int i = 0; i < mRealPoints.length; i++) {
                    mRealPoints[i] = Double.parseDouble(mMarketDatas[TRADE_IDX_BASE[i]][2]);
                }
                updateStockData(mViewPager.getCurrentItem());
            }
        };

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String message = (String) msg.obj;
                Toast.makeText(mMainActivity, message, Toast.LENGTH_LONG).show();
                if (message.equals("更新成功！"))
                    mTextViewDate.setText(updateDate);
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
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == 0) {
                    itemIndex = mViewPager.getCurrentItem();
                    updateStockData(itemIndex);
                }
            }
        });
        mTabLayout = (TabLayout) view.findViewById(R.id.tabLayoutTrade);
        mTabLayout.setupWithViewPager(mViewPager);

        mTextViewBaseName = (TextView) view.findViewById(R.id.textViewTradeBaseName);
        mTextViewSelfName = (TextView) view.findViewById(R.id.textViewTradeSelfName);
        mTextViewBaseData = (TextView) view.findViewById(R.id.textViewTradeBaseData);
        mTextViewSelfData = (TextView) view.findViewById(R.id.textViewTradeSelfData);
        updateStockData(itemIndex);

        fileUtility.importDataFile1("investor/data/" + TRADE_NAMES[0] + ".txt");
        latestDate = fileUtility.dateList1.get(fileUtility.rows1 - 1);
        mTextViewDate = (TextView) view.findViewById(R.id.textViewTradeDate);
        mTextViewDate.setText(latestDate);
        checkDataUpdate();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.currFragTag = FRAGMENT_FLAG_TRADE;
    }

    public void updateStockData(int index) {
        String[][] marketDatas = mMarketDatas;
        mTextViewBaseName.setText(TRADE_BASES[index] + ": ");
        mTextViewBaseData.setText(marketDatas[TRADE_IDX_BASE[index]][4] + "% " + marketDatas[TRADE_IDX_BASE[index]][2]);
        mTextViewBaseData.setTextColor((Double.parseDouble(marketDatas[TRADE_IDX_BASE[index]][3]) > 0) ? Color.RED : Color.rgb(0, 200, 0));

        if (TRADE_IDX_SELF[index] != TRADE_IDX_BASE[index]) {
            mTextViewSelfName.setVisibility(View.VISIBLE);
            mTextViewSelfData.setVisibility(View.VISIBLE);
            mTextViewSelfName.setText(TRADE_NAMES[index] + ": ");
            mTextViewSelfData.setText(marketDatas[TRADE_IDX_SELF[index]][4] + "% " + marketDatas[TRADE_IDX_SELF[index]][2]);
            mTextViewSelfData.setTextColor((Double.parseDouble(marketDatas[TRADE_IDX_SELF[index]][3]) > 0) ? Color.RED : Color.rgb(0, 200, 0));
        } else {
            mTextViewSelfName.setVisibility(View.INVISIBLE);
            mTextViewSelfData.setVisibility(View.INVISIBLE);
        }
    }

    private void checkDataUpdate() {
        String currentDate = TimeUtility.getCurrentDate();
        if ((!currentDate.equals(latestDate)) && (isCheckTime())) {
            Thread udt = new UpdateDataThread();
            udt.start();
        }
    }

    class UpdateDataThread extends Thread {
        @Override
        public void run() {
            Message msg = mHandler.obtainMessage();
            String storageDir = Environment.getExternalStorageDirectory().toString();
            for (int i = 0; i < TRADE_NAMES.length; i++) {
                String filePath = storageDir + "/investor/data/" + TRADE_NAMES[i] + ".txt";
                File file = new File(filePath);
                if (file.exists()) {
                    try {
                        String urlStr = "http://hq.sinajs.cn/list=s" + TRADE_CODES[i];
                        HttpUtility httpUtility = new HttpUtility();
                        String httpStr = httpUtility.getData(urlStr);
                        if (httpStr.equals("")) {
                            msg.obj = "Lose connection 1 !";
                            mHandler.sendMessage(msg);
                            return;
                        } else if (httpStr.contains("\"\"")) {
                            msg.obj = "找不到对应的股票！";
                            mHandler.sendMessage(msg);
                            return;
                        } else {
                            String[] strs = httpStr.substring(httpStr.indexOf("\"") + 1, httpStr.lastIndexOf("\"")).split(",");
                            updateDate = strs[30].replace("-", "/");
                            if (updateDate.equals(latestDate))
                                return;
                            String dataStr = String.format("%s\t%.2f\t%.2f\t%.2f\t%.2f\r", updateDate,
                                    Double.parseDouble(strs[1]), Double.parseDouble(strs[4]), Double.parseDouble(strs[5]), Double.parseDouble(strs[3]));
                            PrintWriter pw = new PrintWriter(new FileWriter(file, true));
                            pw.println(dataStr);
                            pw.close();
                        }
                    } catch (IOException | NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
            msg.obj = "更新成功！";
            mHandler.sendMessage(msg);
        }
    }
}
