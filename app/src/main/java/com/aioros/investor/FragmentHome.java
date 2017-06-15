package com.aioros.investor;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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


public class FragmentHome extends BaseFragment {

    private static final String TAG = "FragmentHome";
    private MainActivity mMainActivity;
    private ListView mListView;
    private StockAdapter mMsgAdapter;
    private List<StockBean> stockBeanList = new ArrayList<StockBean>();
    private Handler handler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layoutHome = inflater.inflate(R.layout.home, container, false);
        Log.d(TAG, "onCreateView---->");
        mMainActivity = (MainActivity) getActivity();
        mFragmentManager = getActivity().getFragmentManager();
        mListView = (ListView) layoutHome.findViewById(R.id.listview_message);
        mMsgAdapter = new StockAdapter(stockBeanList, mMainActivity);
        mListView.setAdapter(mMsgAdapter);
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

        return layoutHome;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e(TAG, "onAttach-----");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate------");
        stockBeanList.add(new StockBean("上证指数", "000001", "--", "0.00", "0.00%"));
        stockBeanList.add(new StockBean("深证成指", "399001", "--", "0.00", "0.00%"));
        stockBeanList.add(new StockBean("创业板指", "399006", "--", "0.00", "0.00%"));
        stockBeanList.add(new StockBean("沪深300", "000300", "--", "0.00", "0.00%"));
        stockBeanList.add(new StockBean("中证500", "000905", "--", "0.00", "0.00%"));
        stockBeanList.add(new StockBean("腾讯济安", "000847", "--", "0.00", "0.00%"));
        stockBeanList.add(new StockBean("养老产业", "399812", "--", "0.00", "0.00%"));
        stockBeanList.add(new StockBean("医药100", "000978", "--", "0.00", "0.00%"));
        stockBeanList.add(new StockBean("CSSW证券", "399707", "--", "0.00", "0.00%"));
        stockBeanList.add(new StockBean("中证军工", "399967", "--", "0.00", "0.00%"));
        stockBeanList.add(new StockBean("中证环保", "000827", "--", "0.00", "0.00%"));

        // 在主线程中声明一个消息处理对象Handler
        handler = new Handler() {
            // 重载消息处理方法，用于接收和处理WorkerThread发送的消息
            @Override
            public void handleMessage(Message msg) {
                String[] message = (String[]) msg.obj;
                String[] strs;
                for (int i = 0; i < message.length; i++) {
                    strs = message[i].substring(message[i].indexOf("\"") + 1, message[i].lastIndexOf("\"")).split("~");
                    stockBeanList.get(i).setStockValue(strs[3]);
                    stockBeanList.get(i).setStockScope(strs[4]);
                    stockBeanList.get(i).setStockRatio(strs[5] + "%");
                }
                mMsgAdapter.notifyDataSetChanged();
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
                handler.postDelayed(this, delay);
            }
        };
        if (!isTradeTime()) {
            new NetworkThread().start();
            handler.postDelayed(runnable, 60000);
        } else {
            handler.postDelayed(runnable, 1);
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

    /**
     * 内部WorkerThread类：NetworkThread
     */
    class NetworkThread extends Thread {
        @Override
        public void run() {
            String urlStr = "http://qt.gtimg.cn/r=0.8409869808238q=" +
                    "s_sh000001,s_sz399001,s_sz399006,s_sh000300,s_sh000905,s_sh000847,s_sz399812,s_sh000978,s_sz399707,s_sz399967,s_sh000827";
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
                Message msgRx = handler.obtainMessage();
                msgRx.obj = strArray;

                // 发送消息，WorkerThread 向 MainThread 发送消息
                handler.sendMessage(msgRx);
            }
        }
    }

    /*
    * 网易历史数据接口：
    * http://quotes.money.163.com/service/chddata.html?code=0000001&start=20161219&end=20170531&fields=TCLOSE;CHG;PCHG
    * 新浪历史数据接口：
    * http://biz.finance.sina.com.cn/stock/flash_hq/kline_data.php?&symbol=sz002241&end_date=20130806&begin_date=20130101
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
            int ret = httpDownloader.download(urlStr, getResources().getString(R.string.app_dir) + "/data", stockCode + ".txt");
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
        Toast.makeText(mMainActivity, stockBeanList.get(position).toString(), Toast.LENGTH_SHORT).show();
    }

    private boolean mListViewOnItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(mMainActivity, "开始下载" + stockBeanList.get(position).getStockName(), Toast.LENGTH_SHORT).show();

        String stockCode = stockBeanList.get(position).getStockCode();
        Thread dft = new DownFileThread(stockCode);
        dft.start();

        return true;
    }
}
