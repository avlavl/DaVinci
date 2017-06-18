package com.aioros.investor;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.aioros.investor.BottomControlPanel.BottomPanelCallback;

public class MainActivity extends FragmentActivity implements BottomPanelCallback {
    BottomControlPanel mBottomPanel = null;
    HeadControlPanel mHeadPanel = null;

    private FragmentManager mFragmentManager = null;
    private FragmentTransaction mFragmentTransaction = null;

    private FragmentHome mFragmentHome;
    private FragmentTrade mFragmentTrade;
    private FragmentInvest mFragmentInvest;
    private FragmentChance mFragmentChance;
    private FragmentMore mFragmentMore;

    public static String currFragTag = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        mFragmentManager = getFragmentManager();
        setDefaultFirstFragment(Constant.FRAGMENT_FLAG_HOME);
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
            currFragTag = tag;
            mFragmentTransaction = null;
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
}
