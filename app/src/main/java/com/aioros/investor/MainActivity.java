package com.aioros.investor;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import com.aioros.investor.BottomControlPanel.BottomPanelCallback;

import static com.aioros.investor.Constant.*;
import static com.aioros.investor.TimeUtility.isCheckTime;
import static com.aioros.investor.TimeUtility.isTradeTime;

public class MainActivity extends FragmentActivity implements BottomPanelCallback {
    BottomControlPanel mBottomPanel = null;
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
    public Handler mChanceHandler;
    public String[][] mMarketDatas = new String[21][6];

    private Handler mHandler;
    private Handler mAiHandler;
    private String mStockCodeStr;
    private String mAiTraderDate;
    private FileUtility fileUtility;

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
        setDefaultFirstFragment(FRAGMENT_FLAG_HOME);

        verifyStoragePermissions(this);

        mAiHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String message = (String) msg.obj;
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                if (message.equals("智能选股已更新！")) {
                    fileUtility.importStockData("investor/data/aiTrader.txt");
                    mStockCodeStr = fileUtility.stockCodeStr;
                    mAiTraderDate = fileUtility.aiTraderDate;
                    for (int i = 0; i < NUMBER_STOCK; i++) {
                        mMarketDatas[INDEX_STOCK + i][5] = fileUtility.probList.get(i);
                    }

                }
            }
        };

        fileUtility = new FileUtility();
        fileUtility.importStockData("investor/data/aiTrader.txt");
        mStockCodeStr = fileUtility.stockCodeStr;
        mAiTraderDate = fileUtility.aiTraderDate;
        for (int i = 0; i < NUMBER_STOCK; i++) {
            mMarketDatas[INDEX_STOCK + i][5] = fileUtility.probList.get(i);
        }
        checkAiTraderUpdate();

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

        new NetworkThread().start();
        mHandler.postDelayed(runnable, 3000);

        //判断SDK版本是否大于等于4.4  因为该属性只有19版本才能设置
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
//            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
//            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS |
//                    localLayoutParams.flags);
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void initUI() {
        mBottomPanel = (BottomControlPanel) findViewById(R.id.layoutBottomPanel);
        if (mBottomPanel != null) {
            mBottomPanel.initBottomPanel();
            mBottomPanel.setBottomCallback(this);
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
        if ((itemId & BTN_FLAG_HOME) != 0) {
            tag = FRAGMENT_FLAG_HOME;
        } else if ((itemId & BTN_FLAG_TRADE) != 0) {
            tag = FRAGMENT_FLAG_TRADE;
        } else if ((itemId & BTN_FLAG_INVEST) != 0) {
            tag = FRAGMENT_FLAG_INVEST;
        } else if ((itemId & BTN_FLAG_CHANCE) != 0) {
            tag = FRAGMENT_FLAG_CHANCE;
        } else if ((itemId & BTN_FLAG_MORE) != 0) {
            tag = FRAGMENT_FLAG_MORE;
        }

        setTabSelection(tag); //切换Fragment
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (TextUtils.equals(FRAGMENT_FLAG_MORE, currFragTag) && (keyCode == KeyEvent.KEYCODE_BACK) && mFragmentMore.mWebView.canGoBack()) {
            mFragmentMore.mWebView.goBack();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
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
            case FRAGMENT_FLAG_HOME:
                return mFragmentHome;
            case FRAGMENT_FLAG_TRADE:
                return mFragmentTrade;
            case FRAGMENT_FLAG_INVEST:
                return mFragmentInvest;
            case FRAGMENT_FLAG_CHANCE:
                return mFragmentChance;
            case FRAGMENT_FLAG_MORE:
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
        attachFragment(R.id.layoutContent, getFragment(tag), tag);
        commitTransactions(tag);
    }


    /* 设置选中的Tag */
    public void setTabSelection(String tag) {
        // 开启一个Fragment事务
        mFragmentTransaction = mFragmentManager.beginTransaction();
        if (TextUtils.equals(tag, FRAGMENT_FLAG_HOME)) {
            if (mFragmentHome == null) {
                mFragmentHome = new FragmentHome();
            }
        } else if (TextUtils.equals(tag, FRAGMENT_FLAG_TRADE)) {
            if (mFragmentTrade == null) {
                mFragmentTrade = new FragmentTrade();
            }
        } else if (TextUtils.equals(tag, FRAGMENT_FLAG_INVEST)) {
            if (mFragmentInvest == null) {
                mFragmentInvest = new FragmentInvest();
            }
        } else if (TextUtils.equals(tag, FRAGMENT_FLAG_CHANCE)) {
            if (mFragmentChance == null) {
                mFragmentChance = new FragmentChance();
            }
        } else if (TextUtils.equals(tag, FRAGMENT_FLAG_MORE)) {
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

    /* 推送通知 */
    private void pushNotice(String title, String text) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_app_logo);
        builder.setContentTitle(title); //下拉通知栏标题
        builder.setContentText(text); //下拉通知栏内容
        builder.setAutoCancel(true);
        builder.setSound(Uri.fromFile(new File("/system/media/audio/notifications/CrystalRing.ogg")));
        builder.setDefaults(Notification.DEFAULT_VIBRATE);
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, 0);
        builder.setContentIntent(contentIntent); //点击跳转的intent
        Notification notification = builder.build();
        notificationManager.notify((int) System.currentTimeMillis(), notification);
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
                String urlStr = ((mDataSource == 0) ? "http://qt.gtimg.cn/r=0.8409869808238q=" : "http://hq.sinajs.cn/list=") + STOCK_CODE_STR + mStockCodeStr;
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
                    mMarketDatas[i][1] = strs[2];
                    mMarketDatas[i][2] = strs[3];
                    mMarketDatas[i][3] = strs[4];
                    mMarketDatas[i][4] = strs[5];
                } else {
                    String[] strs = items[i].substring(items[i].indexOf("\"") + 1, items[i].lastIndexOf("\"")).split(",");
                    mMarketDatas[i][0] = strs[0];
                    mMarketDatas[i][1] = items[i].substring(items[i].indexOf("=") - 6, items[i].lastIndexOf("="));
                    mMarketDatas[i][2] = String.format("%.2f", Double.parseDouble(strs[1]));
                    mMarketDatas[i][3] = String.format("%.2f", Double.parseDouble(strs[2]));
                    mMarketDatas[i][4] = strs[3];
                }
            }

            if (mHomeHandler != null) {
                Message msgRx = mHomeHandler.obtainMessage(); // 使用Handler对象创建一个消息体
                msgRx.obj = mMarketDatas;
                mHomeHandler.sendMessage(msgRx); // 发送消息，WorkerThread 向 MainThread 发送消息
            }
            if (mTradeHandler != null) {
                Message msgRx = mTradeHandler.obtainMessage();
                msgRx.obj = mMarketDatas;
                mTradeHandler.sendMessage(msgRx);
            }
            if (mInvestHandler != null) {
                Message msgRx = mInvestHandler.obtainMessage();
                msgRx.obj = mMarketDatas;
                mInvestHandler.sendMessage(msgRx);
            }
            if (mChanceHandler != null) {
                Message msgRx = mChanceHandler.obtainMessage();
                msgRx.obj = mMarketDatas;
                mChanceHandler.sendMessage(msgRx);
            }
        }
    }

    private void checkAiTraderUpdate() {
        String currentDate = TimeUtility.getCurrentDateSimple();
        if ((!currentDate.equals(mAiTraderDate)) && (isCheckTime())) {
            Thread uatt = new UpdateAiTraderThread();
            uatt.start();
        }
    }

    class UpdateAiTraderThread extends Thread {
        @Override
        public void run() {
            Message msg = mAiHandler.obtainMessage();
            String storageDir = Environment.getExternalStorageDirectory().toString();
            String filePath = storageDir + "/investor/data/aiTrader.txt";
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }
            try {
                String urlStr = "http://120.55.49.62/show.php";
                HttpUtility httpUtility = new HttpUtility();
                String httpStr = httpUtility.getHtmlData(urlStr);
                if (httpStr.equals("")) {
                    msg.obj = "网络无连接！";
                    mAiHandler.sendMessage(msg);
                    return;
                } else if (httpStr.contains("\"\"")) {
                    msg.obj = "找不到对应的股票！";
                    mAiHandler.sendMessage(msg);
                    return;
                } else {
                    String str = httpStr.substring(3, httpStr.indexOf("</br>") - 4);
                    String strs[] = str.split("</p><p>");
                    PrintWriter pw = new PrintWriter(new FileWriter(file, true));
                    for (int i = 0; i < strs.length; i++) {
                        pw.println(strs[i]);
                    }
                    pw.close();
                }
            } catch (IOException | NumberFormatException e) {
                e.printStackTrace();
            }
            msg.obj = "智能选股已更新！";
            mAiHandler.sendMessage(msg);
        }
    }
}
