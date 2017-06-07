package com.aioros.investor;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.aioros.investor.BottomControlPanel.BottomPanelCallback;

public class MainActivity extends Activity implements BottomPanelCallback {
    BottomControlPanel bottomPanel = null;
    HeadControlPanel headPanel = null;

    private FragmentManager fragmentManager = null;
    private FragmentTransaction fragmentTransaction = null;

//    private FragmentHome fragmentHome;
//    private FragmentTrade fragmentTrade;
//    private FragmentInvest fragmentInvest;
//    private FragmentChance fragmentChance;
//    private FragmentOther fragmentOther;

    public static String currFragTag = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        fragmentManager = getFragmentManager();
        setDefaultFirstFragment(Constant.FRAGMENT_FLAG_HOME);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void initUI() {
        bottomPanel = (BottomControlPanel) findViewById(R.id.bottom_layout);
        if (bottomPanel != null) {
            bottomPanel.initBottomPanel();
            bottomPanel.setBottomCallback(this);
        }
        headPanel = (HeadControlPanel) findViewById(R.id.head_layout);
        if (headPanel != null) {
            headPanel.initHeadPanel();
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
        } else if ((itemId & Constant.BTN_FLAG_OTHER) != 0) {
            tag = Constant.FRAGMENT_FLAG_OTHER;
        }
        setTabSelection(tag); //切换Fragment
        headPanel.setMiddleTitle(tag); //切换标题
        headPanel.setRightTitle(tag); //切换副标题
    }

    private void setDefaultFirstFragment(String tag) {
        Log.i("Aioros", "setDefaultFirstFragment enter... currFragTag = " + currFragTag);
        setTabSelection(tag);
        bottomPanel.defaultBtnChecked();
        Log.i("Aioros", "setDefaultFirstFragment exit...");
    }

    private void commitTransactions(String tag) {
        if (fragmentTransaction != null && !fragmentTransaction.isEmpty()) {
            fragmentTransaction.commit();
            currFragTag = tag;
            fragmentTransaction = null;
        }
    }

    private FragmentTransaction ensureTransaction() {
        if (fragmentTransaction == null) {
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        }
        return fragmentTransaction;
    }

    private void attachFragment(int layout, Fragment f, String tag) {
        if (f != null) {
            if (f.isDetached()) {
                ensureTransaction();
                fragmentTransaction.attach(f);
            } else if (!f.isAdded()) {
                ensureTransaction();
                fragmentTransaction.add(layout, f, tag);
            }
        }
    }

    private Fragment getFragment(String tag) {
        Fragment f = fragmentManager.findFragmentByTag(tag);
        if (f == null) {
            Toast.makeText(getApplicationContext(), "fragment = null tag = " + tag, Toast.LENGTH_SHORT).show();
            f = BaseFragment.newInstance(getApplicationContext(), tag);
        }
        return f;
    }

    private void detachFragment(Fragment f) {
        if (f != null && !f.isDetached()) {
            ensureTransaction();
            fragmentTransaction.detach(f);
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
        fragmentTransaction = fragmentManager.beginTransaction();
        /*if (TextUtils.equals(tag, Constant.FRAGMENT_FLAG_HOME)) {
            if (fragmentHome == null) {
                fragmentHome = new FragmentHome();
            }

        } else if (TextUtils.equals(tag, Constant.FRAGMENT_FLAG_TRADE)) {
            if (fragmentTrade == null) {
                fragmentTrade = new FragmentTrade();
            }

        } else if (TextUtils.equals(tag, Constant.FRAGMENT_FLAG_INVEST)) {
            if (fragmentInvest == null) {
                fragmentInvest = new FragmentInvest();
            }

        } else if (TextUtils.equals(tag, Constant.FRAGMENT_FLAG_CHANCE)) {
            if (fragmentChance == null) {
                fragmentChance = new FragmentChance();
            }
        } else if (TextUtils.equals(tag, Constant.FRAGMENT_FLAG_OTHER)) {
            if (fragmentOther == null) {
                fragmentOther = new FragmentOther();
            }
        }*/
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
