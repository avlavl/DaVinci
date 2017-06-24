package com.aioros.investor;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by aizhang on 2017/6/7.
 */

public class FragmentHome extends BaseFragment {

    private static final String TAG = "FragmentHome";
    private MainActivity mMainActivity;
    private ListView mListView;
    private AdapterListViewStock mAdapterListView;
    private List<StockBean> mStockBeanList = new ArrayList<StockBean>();
    private Handler mHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        Log.d(TAG, "onCreateView---->");
        mMainActivity = (MainActivity) getActivity();
        mFragmentManager = getActivity().getFragmentManager();
        mListView = (ListView) view.findViewById(R.id.listview_stock);
        mAdapterListView = new AdapterListViewStock(mMainActivity, mStockBeanList);
        mListView.setAdapter(mAdapterListView);
        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mListViewOnItemClick(parent, view, position, id);
            }

        });
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return mListViewOnItemLongClick(parent, view, position, id);
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e(TAG, "onAttach-----");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate------");
        mStockBeanList.add(new StockBean("上证指数", "000001", "--", "0.00", "0.00%"));
        mStockBeanList.add(new StockBean("深证成指", "399001", "--", "0.00", "0.00%"));
        mStockBeanList.add(new StockBean("创业板指", "399006", "--", "0.00", "0.00%"));
        mStockBeanList.add(new StockBean("沪深300", "000300", "--", "0.00", "0.00%"));
        mStockBeanList.add(new StockBean("中证500", "000905", "--", "0.00", "0.00%"));
        mStockBeanList.add(new StockBean("腾讯济安", "000847", "--", "0.00", "0.00%"));
        mStockBeanList.add(new StockBean("养老产业", "399812", "--", "0.00", "0.00%"));
        mStockBeanList.add(new StockBean("医药100", "000978", "--", "0.00", "0.00%"));
        mStockBeanList.add(new StockBean("CSSW证券", "399707", "--", "0.00", "0.00%"));
        mStockBeanList.add(new StockBean("中证军工", "399967", "--", "0.00", "0.00%"));
        mStockBeanList.add(new StockBean("中证环保", "000827", "--", "0.00", "0.00%"));

        // 在主线程中声明一个消息处理对象Handler
        mHandler = new Handler() {
            // 重载消息处理方法，用于接收和处理WorkerThread发送的消息
            @Override
            public void handleMessage(Message msg) {
                String[] message = (String[]) msg.obj;
                String[] strs;
                for (int i = 0; i < message.length; i++) {
                    strs = message[i].substring(message[i].indexOf("\"") + 1, message[i].lastIndexOf("\"")).split("~");
                    mStockBeanList.get(i).setmStockValue(strs[3]);
                    mStockBeanList.get(i).setmStockScope(strs[4]);
                    mStockBeanList.get(i).setmStockRatio(strs[5] + "%");
                }
                mAdapterListView.notifyDataSetChanged();
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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e(TAG, "onActivityCreated-------");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG, "onStart----->");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onresume---->");
        MainActivity.currFragTag = Constant.FRAGMENT_FLAG_HOME;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e(TAG, "onpause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e(TAG, "onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e(TAG, "ondestoryView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "ondestory");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach------");
    }

    /*
     * 新浪实时数据接口：
     * http://hq.sinajs.cn/list=sh600000,sh600004
     * http://hq.sinajs.cn/list=s_sz399001
     * 腾讯实时数据接口：
     * http://qt.gtimg.cn/r=0.8409869808238q=s_sz000559,s_sz000913,s_sh600048
     * 网易实时数据接口：
     * http://api.money.126.net/data/feed/1002151,0600036,money.api?callback=_ntes_quote_callback13451765
     */
    class NetworkThread extends Thread {
        @Override
        public void run() {
            String urlStr = "http://qt.gtimg.cn/r=0.8409869808238q=" +
                    "s_sh000001,s_sz399001,s_sz399006,s_sh000300,s_sh000905,s_sh000847,s_sz399812,s_sh000978,s_sz399707,s_sz399967,s_sh000827";
            HttpDownloader httpDownloader = new HttpDownloader();
            String downloadString = httpDownloader.getData(urlStr);
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

    /*
    * 网易历史数据接口：
    * http://quotes.money.163.com/service/chddata.html?code=0000001&start=20170519&end=20170631&fields=TCLOSE;CHG;PCHG
    * 新浪历史数据接口：
    * http://biz.finance.sina.com.cn/stock/flash_hq/kline_data.php?&symbol=sz002241&end_date=20130806&begin_date=20130101
    * http://money.finance.sina.com.cn/quotes_service/api/json_v2.php/CN_MarketData.getKLineData?symbol=sz399707&scale=1200&ma=no&datalen=50
    * 搜狐历史数据接口：
    * http://q.stock.sohu.com/hisHq?code=cn_300228&start=20160930&end=20161231&stat=1&order=A&period=w
    */
    class DownFileThread extends Thread {
        private String stockCode;

        public DownFileThread(String code) {
            stockCode = code;
        }

        @Override
        public void run() {
            String urlStr = "http://quotes.money.163.com/service/chddata.html?code="
                    + ((stockCode.substring(0, 1).equals("0")) ? "0" : "1") + stockCode
                    + "&fields=TCLOSE;CHG;PCHG";
            HttpDownloader httpDownloader = new HttpDownloader();
            int ret = httpDownloader.getFile(urlStr, getResources().getString(R.string.app_dir) + "/data", stockCode + ".txt");
            Looper.prepare();
            Toast.makeText(mMainActivity, (ret == 0) ? "下载成功" : "下载失败", Toast.LENGTH_LONG).show();
            Looper.loop();
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

    private void mListViewOnItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(mMainActivity, mStockBeanList.get(position).toString(), Toast.LENGTH_SHORT).show();
        mMainActivity.mBottomPanel.mBtnTrade.callOnClick();
    }

    private boolean mListViewOnItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(mMainActivity, "开始下载" + mStockBeanList.get(position).getmStockName(), Toast.LENGTH_SHORT).show();

        String stockCode = mStockBeanList.get(position).getmStockCode();
        Thread dft = new DownFileThread(stockCode);
        dft.start();

        return true;
    }
}
