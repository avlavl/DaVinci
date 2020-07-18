package com.aioros.investor;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static com.aioros.investor.Constant.*;
import static com.aioros.investor.TimeUtility.isAiCheckTime;

/**
 * Created by aizhang on 2017/6/7.
 */

public class FragmentChance extends BaseFragment {
    private static final String TAG = "FragmentChance";
    private MainActivity mMainActivity;
    private ListView mListView;
    private AdapterListViewStock mAdapterListView;
    private List<BeanStock> mBeanStockList = new ArrayList<BeanStock>();
    public String[][] mMarketDatas;
    public String[][] mStockDatas = new String[10][5];
    private Button mButtonChance;
    public Handler mHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate------");
        mMainActivity = (MainActivity) getActivity();
        mMarketDatas = mMainActivity.mMarketDatas;
        for (int i = 0; i < NUMBER_STOCK; i++) {
            mBeanStockList.add(new BeanStock(mMarketDatas[INDEX_STOCK + i][0], mMarketDatas[INDEX_STOCK + i][1], mMarketDatas[INDEX_STOCK + i][2], mMarketDatas[INDEX_STOCK + i][3], mMarketDatas[INDEX_STOCK + i][4] + "%", mMarketDatas[INDEX_STOCK + i][5]));
        }

        // 在主线程中声明一个消息处理对象Handler
        mMainActivity.mChanceHandler = new Handler() {
            // 重载消息处理方法，用于接收和处理WorkerThread发送的消息
            @Override
            public void handleMessage(Message msg) {
                mMarketDatas = (String[][]) msg.obj;
                for (int i = 0; i < NUMBER_STOCK; i++) {
                    mBeanStockList.get(i).mStockValue = mMarketDatas[INDEX_STOCK + i][2];
                }
                mAdapterListView.notifyDataSetChanged();
            }
        };

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String message = (String) msg.obj;
                Toast.makeText(mMainActivity, message, Toast.LENGTH_LONG).show();
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chance, container, false);
        Log.d(TAG, "onCreateView---->");
        mFragmentManager = getActivity().getFragmentManager();
        mListView = (ListView) view.findViewById(R.id.listViewChance);
        mAdapterListView = new AdapterListViewStock(mMainActivity, mBeanStockList);
        mListView.setAdapter(mAdapterListView);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mListViewOnItemClick(parent, view, position, id);
            }

        });

        mButtonChance = (Button) view.findViewById(R.id.buttonChance);
        mButtonChance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonOnClick(v);
            }
        });
        mButtonChance.setVisibility(isAiCheckTime() ? View.VISIBLE : View.INVISIBLE);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e(TAG, "onAttach-----");
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
        MainActivity.currFragTag = FRAGMENT_FLAG_CHANCE;
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

    private void mListViewOnItemClick(AdapterView<?> parent, View view, int position, long id) {
    }

    private void buttonOnClick(View v) {
        Thread udt = new UpdateTestThread();
        udt.start();
    }

    class UpdateTestThread extends Thread {
        @Override
        public void run() {
            Message msg = mHandler.obtainMessage();
            try {
                String urlStr = "http://120.55.49.62/show.php";
                HttpUtility httpUtility = new HttpUtility();
                String httpStr = httpUtility.getHtmlData(urlStr);
                String aiUpdateInfo;
                if (httpStr.contains("aitrader")) {
                    String str = httpStr.substring(3, httpStr.indexOf("</br>") - 4);
                    String strs[] = str.split("</p><p>");
                    String codeStr = "";
                    String[] words = strs[0].split(":");
                    aiUpdateInfo = words[2].toUpperCase();
                    ArrayList<String> probList = new ArrayList<>();
                    for (int i = 2; i < 12; i++) {
                        words = strs[i].split(":");
                        if (words.length == 3) {
                            String[] cs = words[0].split("\\.");
                            codeStr += ((i == 2) ? "s_" : ",s_") + cs[1].toLowerCase() + cs[0];
                            probList.add(words[2]);
                        }
                    }
                    int dataSouce = 0;
                    do {
                        urlStr = ((dataSouce == 0) ? "http://qt.gtimg.cn/r=0.8409869808238q=" : "http://hq.sinajs.cn/list=") + codeStr;
                        httpStr = httpUtility.getData(urlStr);
                        if (httpStr.equals("")) {
                            msg.obj = "无网络连接！";
                            mHandler.sendMessage(msg);
                            return;
                        } else if (httpStr.contains("pv_none_match")) {
                            dataSouce = 1;
                        } else if (httpStr.contains("\"\"")) {
                            dataSouce = 0;
                        }
                    } while (httpStr.contains("pv_none_match") || httpStr.contains("\"\""));

                    String[] items = httpStr.split(";");
                    for (int i = 0; i < items.length; i++) {
                        if (dataSouce == 0) {
                            strs = items[i].substring(items[i].indexOf("\"") + 1, items[i].lastIndexOf("\"")).split("~");
                            mStockDatas[i][0] = strs[1];
                            mStockDatas[i][1] = strs[2];
                            mStockDatas[i][2] = strs[3];
                            mStockDatas[i][3] = strs[5];
                        } else {
                            strs = items[i].substring(items[i].indexOf("\"") + 1, items[i].lastIndexOf("\"")).split(",");
                            mStockDatas[i][0] = strs[0];
                            mStockDatas[i][1] = items[i].substring(items[i].indexOf("=") - 6, items[i].lastIndexOf("="));
                            mStockDatas[i][2] = String.format("%.2f", Double.parseDouble(strs[1]));
                            mStockDatas[i][3] = strs[3];
                        }
                        mStockDatas[i][4] = probList.get(i);
                    }

                    Looper.prepare();
                    AlertDialog alertDialog = new AlertDialog.Builder(mMainActivity).create();
                    alertDialog.show();
                    Window window = alertDialog.getWindow();
                    window.setContentView(R.layout.dialog_chance_stocks);
                    TextView topx = (TextView) window.findViewById(R.id.textViewChanceTopx);
                    topx.setText(aiUpdateInfo);

                    ListView listView = (ListView) window.findViewById(R.id.listViewChanceStocks);
                    AdapterListViewChanceStocks adapterListView = new AdapterListViewChanceStocks(mMainActivity, mStockDatas);
                    listView.setAdapter(adapterListView);
                    Looper.loop();
                } else {
                    msg.obj = "无法访问服务器！";
                    mHandler.sendMessage(msg);
                    return;
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }
}
