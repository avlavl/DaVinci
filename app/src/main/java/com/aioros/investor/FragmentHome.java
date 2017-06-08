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
import java.util.List;


public class FragmentHome extends BaseFragment {

    private static final String TAG = "FragmentHome";
    private MainActivity mMainActivity;
    private ListView mListView;
    private StockAdapter mMsgAdapter;
    private List<StockBean> stockBeanList = new ArrayList<StockBean>();
    private Handler handlerTx;    // 消息处理对象
    private Handler handlerRx;    // 消息处理对象

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
                Toast.makeText(mMainActivity, stockBeanList.get(position).toString(), Toast.LENGTH_SHORT).show();
                Message msg = handlerTx.obtainMessage();
                msg.obj = "6000300";
                // 发送消息，MainThread 向 WorkerThread 发送消息
                handlerTx.sendMessage(msg);
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
        stockBeanList.add(new StockBean(R.drawable.property_select, "上证指数", "000001", "3100.00", "0.20%", "61.00"));
        stockBeanList.add(new StockBean(R.drawable.fund_select, "深证成指", "399001", "3100.00", "0.20%", "61.00"));
        stockBeanList.add(new StockBean(R.drawable.news_select, "创业板指", "399006", "3100.00", "0.20%", "61.00"));
        stockBeanList.add(new StockBean(R.drawable.home_select, "沪深300", "000300", "3100.00", "0.20%", "61.00"));
        stockBeanList.add(new StockBean(R.drawable.trade_select, "中证500", "000905", "3100.00", "0.20%", "61.00"));
        stockBeanList.add(new StockBean(R.drawable.market_select, "腾讯济安", "000847", "3100.00", "0.20%", "61.00"));
        stockBeanList.add(new StockBean(R.drawable.material_select, "养老产业", "399812", "3100.00", "0.20%", "61.00"));
        stockBeanList.add(new StockBean(R.drawable.news_select, "医药100", "000978", "3100.00", "0.20%", "61.00"));
        stockBeanList.add(new StockBean(R.drawable.notice_select, "CSSW证券", "399707", "3100.00", "0.20%", "61.00"));
        stockBeanList.add(new StockBean(R.drawable.optional_select, "中证军工", "399967", "3100.00", "0.20%", "61.00"));
        stockBeanList.add(new StockBean(R.drawable.trade_select, "中证环保", "000827", "3100.00", "0.20%", "61.00"));

        // 在主线程中声明一个消息处理对象Handler
        handlerRx = new Handler() {
            // 重载消息处理方法，用于接收和处理WorkerThread发送的消息
            @Override
            public void handleMessage(Message msg) {
                String[] message = (String[]) msg.obj;
                String[] strs;
                //System.out.println(message);
                // 将WorkerThread发送的消息内容显示在TextView中
                //textView.setText(message);
                for (int i = 0; i < message.length; i++) {
                    strs = message[i].substring(message[i].indexOf("\"") + 1, message[i].lastIndexOf("\"")).split("~");
                    //final String formatedString = strs[1] + "\t" + strs[2] + "\n最新：" + strs[3] + "\t涨跌：" + strs[4] + "\t涨幅" + strs[5] + "%";
                    stockBeanList.get(i).setStockValue(strs[3]);
                    stockBeanList.get(i).setStockRatio(strs[5] + "%");
                    stockBeanList.get(i).setStockScope(strs[4]);
                }
                mMsgAdapter.notifyDataSetChanged();
            }
        };
        NetworkThread nwThread = new NetworkThread();
        nwThread.start();
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
            //1. 准备Looper
            Looper.prepare();

            //2. WorkerThread中创建一个消息处理对象Handler
            handlerTx = new Handler() {
                // 重载消息处理方法，用于接收和处理WorkerThread发送的消息
                @Override
                public void handleMessage(Message msg) {
                    System.out.println("WorkerThread收到消息: " + msg.obj);
                    //String code = (String) msg.obj;

                    String urlStr = "http://qt.gtimg.cn/r=0.8409869808238q=" +
                            "s_sh000001,s_sz399001,s_sz399006,s_sh000300,s_sh000905,s_sh000847,s_sz399812,s_sh000978,s_sz399707,s_sz399967,s_sh000827";
                    HttpDownloader httpDownloader = new HttpDownloader();
                    String downloadString = httpDownloader.download(urlStr);
                    if (downloadString.contains("pv_none_match")) {
                        Looper.prepare();
                        Toast.makeText(mMainActivity, "找不到对应的股票！", Toast.LENGTH_LONG).show();
                        Looper.loop();
                    } else {
                        String[] strArray = downloadString.split(";");
//                        String[] strs = downloadString.substring(downloadString.indexOf("\"") + 1, downloadString.lastIndexOf("\"")).split("~");
//                        final String formatedString = strs[1] + "\t" + strs[2] + "\n最新：" + strs[3] + "\t涨跌：" + strs[4] + "\t涨幅" + strs[5] + "%";
                        // 使用主线程Handler对象创建一个消息体
                        Message msgRx = handlerRx.obtainMessage();
                        msgRx.obj = strArray;

                        // 发送消息，WorkerThread 向 MainThread 发送消息
                        handlerRx.sendMessage(msgRx);
                    }
                }
            };

            //3. 调用Looper.loop()方法，从消息队列中不断获取消息，然后调用该消息对象的Handler对象的handleMessage(Message msg)进行处理
            // 如果消息队列中没有消息，则Looper线程阻塞等待
            Looper.loop();

        }
    }
}
