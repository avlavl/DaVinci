package com.aioros.investor;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by aizhang on 2017/6/7.
 */

public class FragmentInvest extends BaseFragment {
    private static final String TAG = "FragmentInvest";
    private MainActivity mMainActivity;
    private AdapterPagerInvest mAdapterPager;
    private List<InvestBean> mInvestBeanList = new ArrayList<InvestBean>();
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private Handler mHandler;

    public String stockName = "W399707";
    public String stockCode = "000001";
    public int column = 0;
    public int rows = 0;
    public ArrayList<String> dateList;
    public ArrayList<Double> closeList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate------");
        mInvestBeanList.add(new InvestBean(1000, 7.5, 20, 1.5, 30));
        mInvestBeanList.add(new InvestBean(1000, 7, 20, 1.5, 20));
        mInvestBeanList.add(new InvestBean(1400, 10, 20, 1.5, 20));
        String storageDir = Environment.getExternalStorageDirectory().toString() + "/invester/data/";
        importFile(storageDir + "W399707.txt");

        // 在主线程中声明一个消息处理对象Handler
        mHandler = new Handler() {
            // 重载消息处理方法，用于接收和处理WorkerThread发送的消息
            @Override
            public void handleMessage(Message msg) {
                String[] message = (String[]) msg.obj;
                String[] strs;
                for (int i = 0; i < message.length; i++) {
                    strs = message[i].substring(message[i].indexOf("\"") + 1, message[i].lastIndexOf("\"")).split("~");
                    mInvestBeanList.get(i).setmRealPoint(strs[3]);
                }
                double basePoint = mInvestBeanList.get(1).getmStartPoint() + rows * mInvestBeanList.get(1).getmSlope();
                mInvestBeanList.get(1).setmBasePoint(Double.toString(basePoint));

                double diffRate = Double.parseDouble(mInvestBeanList.get(1).getmRealPoint()) / basePoint;
                double divisor = mInvestBeanList.get(1).getmDivisor();
                double input = (basePoint / divisor) / Math.pow(diffRate, mInvestBeanList.get(1).getmDiffCoef());
                if (diffRate <= 1) {
                    mInvestBeanList.get(1).setmQuota(String.format("%.2f", input));
                } else {
                    mInvestBeanList.get(1).setmQuota("无需投资");
                }

                mAdapterPager.notifyDataSetChanged();
            }
        };

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                boolean flag = isTradeTime();
                int delay = flag ? 3000 : 60000;
                if (flag) {
                    new NetworkThread().start();
                }
                mHandler.postDelayed(this, delay);
            }
        };
        if (!isTradeTime()) {
            new NetworkThread().start();
            mHandler.postDelayed(runnable, 60000);
        } else {
            mHandler.postDelayed(runnable, 1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_invest, container, false);
        Log.d(TAG, "onCreateView---->");
        mMainActivity = (MainActivity) getActivity();
        mFragmentManager = getActivity().getFragmentManager();
        mAdapterPager = new AdapterPagerInvest(mMainActivity, mInvestBeanList);
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager_invest);
        mViewPager.setAdapter(mAdapterPager);
        mTabLayout = (TabLayout) view.findViewById(R.id.tablayout_invest);
        mTabLayout.setupWithViewPager(mViewPager);
        mAdapterPager.notifyDataSetChanged();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.currFragTag = Constant.FRAGMENT_FLAG_INVEST;
    }

    /**
     * 内部WorkerThread类：NetworkThread
     */
    class NetworkThread extends Thread {
        @Override
        public void run() {
            String urlStr = "http://qt.gtimg.cn/r=0.8409869808238q=s_sz399001,s_sz399707,s_sz399812";
            HttpDownloader httpDownloader = new HttpDownloader();
            String downloadString = httpDownloader.download(urlStr);
            if (downloadString.equals("")) {
                Looper.prepare();
                Toast.makeText(mMainActivity, "请检查网络连接！", Toast.LENGTH_LONG).show();
                Looper.loop();
            } else if (downloadString.contains("pv_none_match")) {
                Looper.prepare();
                Toast.makeText(mMainActivity, "找不到对应的股票！", Toast.LENGTH_LONG).show();
                Looper.loop();
            } else {
                String[] strArray = downloadString.split(";");
                // 使用主线程Handler对象创建一个消息体
                Message msgRx = mHandler.obtainMessage();
                msgRx.obj = strArray;

                // 发送消息，WorkerThread 向 MainThread 发送消息
                mHandler.sendMessage(msgRx);
            }
        }
    }

    private boolean isTradeTime() {
        Calendar cal = Calendar.getInstance();
        if ((cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) || (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY))
            return false;
        if ((cal.get(Calendar.HOUR_OF_DAY) < 9) || (cal.get(Calendar.HOUR_OF_DAY) >= 15))
            return false;
        if ((cal.get(Calendar.HOUR_OF_DAY) < 10) && (cal.get(Calendar.MINUTE) < 30))
            return false;
        if ((cal.get(Calendar.HOUR_OF_DAY) >= 11) && (cal.get(Calendar.MINUTE) > 30) && (cal.get(Calendar.HOUR_OF_DAY) < 13))
            return false;
        return true;
    }

    protected void importFile(String fileName) {
        try {
            File file = new File(fileName);
            InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "gbk");
            BufferedReader br = new BufferedReader(isr);
            String[] words = br.readLine().split("\t");
            stockName = words[1];
            //stockCode = words[0].replaceAll("[\\pP\\p{Punct}]", "");
            words = br.readLine().split("\t");
            column = words.length;
            dateList = new ArrayList<>();
            closeList = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                words = line.split("\t");
                dateList.add(words[0]);
                closeList.add(Double.parseDouble(words[4]));
            }
            rows = dateList.size();
            br.close();
            isr.close();
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
//        updateMarket(rows - 1);
//        evaluated = false;
    }
}
