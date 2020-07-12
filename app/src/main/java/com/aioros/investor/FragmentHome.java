package com.aioros.investor;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static com.aioros.investor.Constant.*;

/**
 * Created by aizhang on 2017/6/7.
 */

public class FragmentHome extends BaseFragment {
    private static final String TAG = "FragmentHome";
    private MainActivity mMainActivity;
    private LinearLayout mLayoutHeadHome;
    private ListView mListView;
    private EditText mEditText;
    private ImageView mImageView;
    private Button mButton;
    private AdapterListViewIndex mAdapterListView;
    private List<BeanStock> mBeanStockList = new ArrayList<BeanStock>();
    public String[][] mMarketDatas;
    private Handler handlerRx;
    private PopupWindow mPopupWindow;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate------");
        mMainActivity = (MainActivity) getActivity();
        for (int i = 0; i < NUMBER_INDEX; i++) {
            mBeanStockList.add(new BeanStock(HOME_STOCK_NAMES[i], HOME_STOCK_CODES[i], "--", "0.00", "0.00%"));
        }

        // 在主线程中声明一个消息处理对象Handler
        mMainActivity.mHomeHandler = new Handler() {
            // 重载消息处理方法，用于接收和处理WorkerThread发送的消息
            @Override
            public void handleMessage(Message msg) {
                mMarketDatas = (String[][]) msg.obj;
                for (int i = 0; i < NUMBER_INDEX; i++) {
                    mBeanStockList.get(i).mStockValue = mMarketDatas[i][2];
                    mBeanStockList.get(i).mStockScope = mMarketDatas[i][3];
                    mBeanStockList.get(i).mStockRatio = mMarketDatas[i][4] + "%";
                }
                mAdapterListView.notifyDataSetChanged();
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        //view.setFitsSystemWindows(true);
        Log.d(TAG, "onCreateView---->");
        mFragmentManager = getActivity().getFragmentManager();
        mListView = (ListView) view.findViewById(R.id.listViewStock);
        mAdapterListView = new AdapterListViewIndex(mMainActivity, mBeanStockList);
        mListView.setAdapter(mAdapterListView);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

        mLayoutHeadHome = (LinearLayout) view.findViewById(R.id.layoutHeadHome);
        mEditText = (EditText) view.findViewById(R.id.editTextStockCode);
        mImageView = (ImageView) view.findViewById(R.id.imageViewDelCode);
        mButton = (Button) view.findViewById(R.id.buttonSearch);

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 监听如果输入串长度大于0那么就显示clear按钮。
                mImageView.setVisibility((s.length() > 0) ? View.VISIBLE : View.INVISIBLE);
            }
        });

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditText.setText("");
            }
        });

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = mEditText.getText().toString();
                if ((str.length() == 6) && str.matches("[0-9]*")) {
                    mEditText.clearFocus();
                    InputMethodManager imm = (InputMethodManager) mMainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mMainActivity.getWindow().getDecorView().getWindowToken(), 0);
                    Thread gft = new getDataThread(str);
                    gft.start();
                } else {
                    Toast.makeText(mMainActivity, "股票代码必须为6位数字！", Toast.LENGTH_LONG).show();
                }
            }
        });

        // 在主线程中声明一个消息处理对象Handler
        handlerRx = new Handler() {
            // 重载消息处理方法，用于接收和处理WorkerThread发送的消息
            @Override
            public void handleMessage(Message msg) {
                String[] message = (String[]) msg.obj;
                showMarketPopup(message);
            }
        };

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
        MainActivity.currFragTag = FRAGMENT_FLAG_HOME;
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
            HttpUtility httpUtility = new HttpUtility();
            int ret = httpUtility.getFile(urlStr, getResources().getString(R.string.app_dir) + "/data", stockCode + ".txt");
            Looper.prepare();
            Toast.makeText(mMainActivity, (ret == 0) ? "下载成功" : "下载失败", Toast.LENGTH_LONG).show();
            Looper.loop();
        }
    }

    class getDataThread extends Thread {
        private String stockCode;

        public getDataThread(String code) {
            stockCode = code;
        }

        @Override
        public void run() {
            try {
                String urlStr = "http://qt.gtimg.cn/r=0.8409869808238q=s_s" + ((Long.parseLong(stockCode) > 500000) ? "h" : "z") + stockCode;
                HttpUtility httpUtility = new HttpUtility();
                String httpStr = httpUtility.getData(urlStr);
                if (httpStr.equals("")) {
                    Looper.prepare();
                    Toast.makeText(mMainActivity, "无网络连接！", Toast.LENGTH_LONG).show();
                    Looper.loop();
                    return;
                }
                if (httpStr.contains("pv_none_match")) {
                    urlStr = "http://qt.gtimg.cn/r=0.8409869808238q=s_s" + ((Long.parseLong(stockCode) > 500000) ? "z" : "h") + stockCode;
                    httpStr = httpUtility.getData(urlStr);
                    if (httpStr.contains("pv_none_match")) {
                        Looper.prepare();
                        Toast.makeText(mMainActivity, "找不到对应的股票！", Toast.LENGTH_LONG).show();
                        Looper.loop();
                        return;
                    }
                }

                String[] strs = httpStr.substring(httpStr.indexOf("\"") + 1, httpStr.lastIndexOf("\"")).split("~");
                // 使用主线程Handler对象创建一个消息体
                Message msgRx = handlerRx.obtainMessage();
                msgRx.obj = strs;

                // 发送消息，WorkerThread 向 MainThread 发送消息
                handlerRx.sendMessage(msgRx);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }

    private void mListViewOnItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(mMainActivity, "点我没用！", Toast.LENGTH_SHORT).show();
        //mMainActivity.mBottomPanel.mBtnTrade.callOnClick();
    }

    private boolean mListViewOnItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(mMainActivity, "开始下载" + mBeanStockList.get(position).mStockName, Toast.LENGTH_SHORT).show();

        String stockCode = mBeanStockList.get(position).mStockCode;
        Thread dft = new DownFileThread(stockCode);
        dft.start();

        return true;
    }

    private void showMarketPopup(String[] strs) {
        //定义了一个全局变量   private PopupWindow mPopupWindow;
        mPopupWindow = new PopupWindow(mMainActivity);
        //设置PopupWindow的宽和高
        mPopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置PopupWindow的背景透明度和颜色
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.argb(210, 255, 255, 255)));
        //设置点击PopupWindow外的其他地方退出PopupWindow。
        mPopupWindow.setFocusable(false);
        mPopupWindow.setOutsideTouchable(true);

        //获得PopupWindow的布局
        View popupView = mMainActivity.getLayoutInflater().inflate(R.layout.popup_window, null);
        TextView textView0 = (TextView) popupView.findViewById(R.id.textViewPopup0);
        textView0.setText(strs[1] + "(" + strs[2] + ")");
        TextView textView1 = (TextView) popupView.findViewById(R.id.textViewPopup1);
        textView1.setText("最新：" + strs[3] + "   涨跌：" + strs[4] + "   幅度: " + strs[5] + "%");
        textView1.setTextColor(Double.parseDouble(strs[4]) > 0 ? Color.rgb(240, 0, 0) : Color.rgb(0, 128, 0));
        //将布局添加到PopupWindow中
        mPopupWindow.setContentView(popupView);

        mPopupWindow.showAsDropDown(mLayoutHeadHome, 0, 30);
    }
}
