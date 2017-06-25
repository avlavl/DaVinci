package com.aioros.investor;

import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

import static com.aioros.investor.TimeUtility.isTradeTime;

/**
 * Created by aizhang on 2017/6/7.
 */

public class FragmentInvest extends BaseFragment {
    private static final String TAG = "FragmentInvest";
    private MainActivity mMainActivity;
    private AdapterPagerInvest mAdapterPager;
    private List<BeanInvest> mBeanInvestList = new ArrayList<BeanInvest>();
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private Handler mHandler;
    private FileUtility fileUtility = new FileUtility();

    public String stockName = "W399707";
    public String stockCode = "000001";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate------");
        mBeanInvestList.add(new BeanInvest(1000, 7.5, 20, 1.5, 30));
        mBeanInvestList.add(new BeanInvest(1000, 7, 20, 1.5, 20));
        mBeanInvestList.add(new BeanInvest(1400, 10, 20, 1.5, 20));
        fileUtility.importDataFile("investor/data/W399707.txt");

        // 在主线程中声明一个消息处理对象Handler
        mHandler = new Handler() {
            // 重载消息处理方法，用于接收和处理WorkerThread发送的消息
            @Override
            public void handleMessage(Message msg) {
                String[] message = (String[]) msg.obj;
                String[] strs;
                for (int i = 0; i < message.length; i++) {
                    strs = message[i].substring(message[i].indexOf("\"") + 1, message[i].lastIndexOf("\"")).split("~");
                    mBeanInvestList.get(i).setmRealPoint(strs[3]);
                }
                double basePoint = mBeanInvestList.get(1).getmStartPoint() + fileUtility.rows * mBeanInvestList.get(1).getmSlope();
                mBeanInvestList.get(1).setmBasePoint(Double.toString(basePoint));

                double diffRate = Double.parseDouble(mBeanInvestList.get(1).getmRealPoint()) / basePoint;
                double divisor = mBeanInvestList.get(1).getmDivisor();
                double input = (basePoint / divisor) / Math.pow(diffRate, mBeanInvestList.get(1).getmDiffCoef());
                if (diffRate <= 1) {
                    mBeanInvestList.get(1).setmQuota(String.format("%.2f", input));
                } else {
                    mBeanInvestList.get(1).setmQuota("无需投资");
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
        mAdapterPager = new AdapterPagerInvest(mMainActivity, mBeanInvestList);
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
            HttpUtility httpUtility = new HttpUtility();
            String httpStr = httpUtility.getData(urlStr);
            if (httpStr.equals("")) {
                Looper.prepare();
                Toast.makeText(mMainActivity, "请检查网络连接！", Toast.LENGTH_LONG).show();
                Looper.loop();
            } else if (httpStr.contains("pv_none_match")) {
                Looper.prepare();
                Toast.makeText(mMainActivity, "找不到对应的股票！", Toast.LENGTH_LONG).show();
                Looper.loop();
            } else {
                String[] strings = httpStr.split(";");
                // 使用主线程Handler对象创建一个消息体
                Message msgRx = mHandler.obtainMessage();
                msgRx.obj = strings;

                // 发送消息，WorkerThread 向 MainThread 发送消息
                mHandler.sendMessage(msgRx);
            }
        }
    }
}
