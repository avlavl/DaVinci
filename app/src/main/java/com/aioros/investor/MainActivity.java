package com.aioros.investor;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.aioros.investor.BottomControlPanel.BottomPanelCallback;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.aioros.investor.TimeUtility.isTradeTime;

public class MainActivity extends FragmentActivity implements BottomPanelCallback {
    BottomControlPanel mBottomPanel = null;
    HeadControlPanel mHeadPanel = null;
    private static Context mContext = null;

    private FragmentManager mFragmentManager = null;
    private FragmentTransaction mFragmentTransaction = null;

    private FragmentHome mFragmentHome;
    private FragmentTrade mFragmentTrade;
    private FragmentInvest mFragmentInvest;
    private FragmentChance mFragmentChance;
    private FragmentMore mFragmentMore;

    public static String currFragTag = "";
    public static int mDataSource = 0;

    public Handler mHomeHandler;
    public Handler mTradeHandler;
    public Handler mInvestHandler;
    public String[][] mMarketDatas = new String[20][4];

    private Handler mHandler;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static Context getContext() {
        return mContext;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        initUI();
        mFragmentManager = getFragmentManager();
        setDefaultFirstFragment(Constant.FRAGMENT_FLAG_HOME);

        verifyStoragePermissions(this);

        // 在主线程中声明一个消息处理对象Handler
        mHandler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                int delay;
                if (isTradeTime()) {
                    delay = 3000;
                    new NetworkThread().start();
                } else {
                    delay = 60000;
                }
                mHandler.postDelayed(this, delay);
            }
        };

        if (isTradeTime()) {
            PushService.cleanAllNotification();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            String time = sdf.format(new Date()) + " 15:01:00";
            PushService.addNotification(time, "有新的数据！", "有新的交易数据，请及时更新数据库。");

            mHandler.postDelayed(runnable, 1);
        } else {
            new NetworkThread().start();
            mHandler.postDelayed(runnable, 60000);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void initUI() {
        mBottomPanel = (BottomControlPanel) findViewById(R.id.layout_bottompanel);
        if (mBottomPanel != null) {
            mBottomPanel.initBottomPanel();
            mBottomPanel.setBottomCallback(this);
        }
        mHeadPanel = (HeadControlPanel) findViewById(R.id.layout_headpanel);
        if (mHeadPanel != null) {
            mHeadPanel.initHeadPanel();
        }
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    /* 处理BottomControlPanel的回调
     * @see com.aioros.investor.BottomControlPanel.BottomPanelCallback#onBottomPanelClick(int)
     */
    @Override
    public void onBottomPanelClick(int itemId) {
        String tag = "";
        if ((itemId & Constant.BTN_FLAG_HOME) != 0) {
            tag = Constant.FRAGMENT_FLAG_HOME;
        } else if ((itemId & Constant.BTN_FLAG_TRADE) != 0) {
            tag = Constant.FRAGMENT_FLAG_TRADE;
        } else if ((itemId & Constant.BTN_FLAG_INVEST) != 0) {
            tag = Constant.FRAGMENT_FLAG_INVEST;
        } else if ((itemId & Constant.BTN_FLAG_CHANCE) != 0) {
            tag = Constant.FRAGMENT_FLAG_CHANCE;
        } else if ((itemId & Constant.BTN_FLAG_MORE) != 0) {
            tag = Constant.FRAGMENT_FLAG_MORE;
        }
        setTabSelection(tag); //切换Fragment
        mHeadPanel.setMiddleTitle(tag); //切换标题
        mHeadPanel.setRightTitle(tag); //切换副标题
    }

    private void setDefaultFirstFragment(String tag) {
        Log.i("Aioros", "setDefaultFirstFragment enter... currFragTag = " + currFragTag);
        setTabSelection(tag);
        mBottomPanel.defaultBtnChecked();
        Log.i("Aioros", "setDefaultFirstFragment exit...");
    }

    private void commitTransactions(String tag) {
        if (mFragmentTransaction != null && !mFragmentTransaction.isEmpty()) {
            mFragmentTransaction.commit();
            mFragmentTransaction = null;
            currFragTag = tag;
        }
    }

    private FragmentTransaction ensureTransaction() {
        if (mFragmentTransaction == null) {
            mFragmentTransaction = mFragmentManager.beginTransaction();
            mFragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        }
        return mFragmentTransaction;
    }

    private Fragment getFragmentOld(String tag) {
        Fragment f = mFragmentManager.findFragmentByTag(tag);
        if (f == null) {
            Toast.makeText(getApplicationContext(), "fragment = null tag = " + tag, Toast.LENGTH_SHORT).show();
            f = BaseFragment.newInstance(getApplicationContext(), tag);
        }
        return f;
    }

    private Fragment getFragment(String tag) {
        switch (tag) {
            case Constant.FRAGMENT_FLAG_HOME:
                return mFragmentHome;
            case Constant.FRAGMENT_FLAG_TRADE:
                return mFragmentTrade;
            case Constant.FRAGMENT_FLAG_INVEST:
                return mFragmentInvest;
            case Constant.FRAGMENT_FLAG_CHANCE:
                return mFragmentChance;
            case Constant.FRAGMENT_FLAG_MORE:
                return mFragmentMore;
            default:
                return null;
        }
    }

    private void attachFragment(int layout, Fragment f, String tag) {
        if (f != null) {
            if (f.isDetached()) {
                ensureTransaction();
                mFragmentTransaction.attach(f);
            } else if (!f.isAdded()) {
                ensureTransaction();
                mFragmentTransaction.add(layout, f, tag);
            }
        }
    }

    private void detachFragment(Fragment f) {
        if (f != null && !f.isDetached()) {
            ensureTransaction();
            mFragmentTransaction.detach(f);
        }
    }

    /* 切换fragment */
    private void switchFragment(String tag) {
        if (TextUtils.equals(tag, currFragTag)) {
            return;
        }
        // 把上一个fragment detach掉
        if (currFragTag != null && !currFragTag.equals("")) {
            detachFragment(getFragment(currFragTag));
        }
        attachFragment(R.id.fragment_content, getFragment(tag), tag);
        commitTransactions(tag);
    }


    /* 设置选中的Tag */
    public void setTabSelection(String tag) {
        // 开启一个Fragment事务
        mFragmentTransaction = mFragmentManager.beginTransaction();
        if (TextUtils.equals(tag, Constant.FRAGMENT_FLAG_HOME)) {
            if (mFragmentHome == null) {
                mFragmentHome = new FragmentHome();
            }
        } else if (TextUtils.equals(tag, Constant.FRAGMENT_FLAG_TRADE)) {
            if (mFragmentTrade == null) {
                mFragmentTrade = new FragmentTrade();
            }
        } else if (TextUtils.equals(tag, Constant.FRAGMENT_FLAG_INVEST)) {
            if (mFragmentInvest == null) {
                mFragmentInvest = new FragmentInvest();
            }
        } else if (TextUtils.equals(tag, Constant.FRAGMENT_FLAG_CHANCE)) {
            if (mFragmentChance == null) {
                mFragmentChance = new FragmentChance();
            }
        } else if (TextUtils.equals(tag, Constant.FRAGMENT_FLAG_MORE)) {
            if (mFragmentMore == null) {
                mFragmentMore = new FragmentMore();
            }
        }
        switchFragment(tag);
    }

    @Override
    protected void onStop() {
        super.onStop();
        currFragTag = "";
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
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
            String httpStr = "";
            do {
                String urlStr = ((mDataSource == 0) ? "http://qt.gtimg.cn/r=0.8409869808238q=" : "http://hq.sinajs.cn/list=") +
                        "s_sh000001,s_sz399001,s_sz399006,s_sh000300,s_sh000905,s_sh000847,s_sz399812,s_sh000978,s_sz399707,s_sh513050,s_sz164906," +
                        "s_sh511990,s_sh511880,s_sh511660,s_sh511810,s_sh511690,s_sh511900,s_sh511830,s_sh511980,s_sh511650";
                HttpUtility httpUtility = new HttpUtility();
                httpStr = httpUtility.getData(urlStr);
                if (httpStr.equals("")) {
                    Looper.prepare();
                    Toast.makeText(MainActivity.this, "无网络连接！", Toast.LENGTH_LONG).show();
                    Looper.loop();
                    return;
                } else if (httpStr.contains("pv_none_match")) {
                    mDataSource = 1;
                } else if (httpStr.contains("\"\"")) {
                    mDataSource = 0;
                }
            } while (httpStr.contains("pv_none_match") || httpStr.contains("\"\""));

            String[] items = httpStr.split(";");
            for (int i = 0; i < items.length; i++) {
                if (mDataSource == 0) {
                    String[] strs = items[i].substring(items[i].indexOf("\"") + 1, items[i].lastIndexOf("\"")).split("~");
                    mMarketDatas[i][0] = strs[1];
                    mMarketDatas[i][1] = strs[3];
                    mMarketDatas[i][2] = strs[4];
                    mMarketDatas[i][3] = strs[5];
                } else {
                    String[] strs = items[i].substring(items[i].indexOf("\"") + 1, items[i].lastIndexOf("\"")).split(",");
                    mMarketDatas[i][0] = strs[0];
                    mMarketDatas[i][1] = String.format("%.3f", Double.parseDouble(strs[1]));
                    mMarketDatas[i][2] = String.format("%.3f", Double.parseDouble(strs[2]));
                    mMarketDatas[i][3] = strs[3];
                }
            }

            if (mHomeHandler != null) {
                // 使用Handler对象创建一个消息体
                Message msgRx = mHomeHandler.obtainMessage();
                msgRx.obj = mMarketDatas;
                // 发送消息，WorkerThread 向 MainThread 发送消息
                mHomeHandler.sendMessage(msgRx);
            }
            if (mTradeHandler != null) {
                // 使用Handler对象创建一个消息体
                Message msgRx = mTradeHandler.obtainMessage();
                msgRx.obj = mMarketDatas;
                // 发送消息，WorkerThread 向 MainThread 发送消息
                mTradeHandler.sendMessage(msgRx);
            }
            if (mInvestHandler != null) {
                // 使用Handler对象创建一个消息体
                Message msgRx = mInvestHandler.obtainMessage();
                msgRx.obj = mMarketDatas;
                // 发送消息，WorkerThread 向 MainThread 发送消息
                mInvestHandler.sendMessage(msgRx);
            }
        }
    }
}
