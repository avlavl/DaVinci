package com.aioros.investor;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;


public class BottomControlPanel extends RelativeLayout implements View.OnClickListener {
    private Context mContext;
    private ImageText btnHome = null;
    private ImageText btnTrade = null;
    private ImageText btnInvest = null;
    private ImageText btnChance = null;
    private ImageText btnOther = null;
    private BottomPanelCallback mBottomCallback = null;
    private List<ImageText> viewList = new ArrayList<ImageText>();

    public interface BottomPanelCallback {
        public void onBottomPanelClick(int itemId);
    }

    public BottomControlPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        btnHome = (ImageText) findViewById(R.id.btn_home);
        btnTrade = (ImageText) findViewById(R.id.btn_trade);
        btnInvest = (ImageText) findViewById(R.id.btn_invest);
        btnChance = (ImageText) findViewById(R.id.btn_chance);
        btnOther = (ImageText) findViewById(R.id.btn_other);
        viewList.add(btnHome);
        viewList.add(btnTrade);
        viewList.add(btnInvest);
        viewList.add(btnChance);
        viewList.add(btnOther);
    }

    public void initBottomPanel() {
        if (btnHome != null) {
            btnHome.setImage(R.drawable.home_normal);
            btnHome.setText("主页");
        }
        if (btnTrade != null) {
            btnTrade.setImage(R.drawable.trade_normal);
            btnTrade.setText("交易");
        }
        if (btnInvest != null) {
            btnInvest.setImage(R.drawable.property_normal);
            btnInvest.setText("定投");
        }
        if (btnChance != null) {
            btnChance.setImage(R.drawable.notice_normal);
            btnChance.setText("机会");
        }
        if (btnOther != null) {
            btnOther.setImage(R.drawable.material_normal);
            btnOther.setText("其它");
        }
        setBtnListener();
    }

    private void setBtnListener() {
        int num = this.getChildCount();
        for (int i = 0; i < num; i++) {
            View v = getChildAt(i);
            if (v != null) {
                v.setOnClickListener(this);
            }
        }
    }

    public void setBottomCallback(BottomPanelCallback bottomCallback) {
        mBottomCallback = bottomCallback;
    }

    @Override
    public void onClick(View v) {
        initBottomPanel();
        int index = -1;
        switch (v.getId()) {
            case R.id.btn_home:
                index = Constant.BTN_FLAG_HOME;
                btnHome.setChecked(index);
                break;
            case R.id.btn_trade:
                index = Constant.BTN_FLAG_TRADE;
                btnTrade.setChecked(index);
                break;
            case R.id.btn_invest:
                index = Constant.BTN_FLAG_INVEST;
                btnInvest.setChecked(index);
                break;
            case R.id.btn_chance:
                index = Constant.BTN_FLAG_CHANCE;
                btnChance.setChecked(index);
                break;
            case R.id.btn_other:
                index = Constant.BTN_FLAG_OTHER;
                btnOther.setChecked(index);
                break;
            default:
                break;
        }
        if (mBottomCallback != null) {
            mBottomCallback.onBottomPanelClick(index);
        }
    }

    public void defaultBtnChecked() {
        if (btnHome != null) {
            btnHome.setChecked(Constant.BTN_FLAG_HOME);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            layoutItems(left, top, right, bottom);
        }
    }

    /**
     * 最左边和最右边的view由母布局的padding进行控制位置。这里需对第2、3个view的位置重新设置
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    private void layoutItems(int left, int top, int right, int bottom) {
        int n = getChildCount();
        if (n == 0) {
            return;
        }
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        //Log.i("Aioros", "paddingLeft = " + paddingLeft + " paddingRight = " + paddingRight);
        int width = right - left;
        int height = bottom - top;
        //Log.i("Aioros", "width = " + width + " height = " + height);
        int allViewWidth = 0;
        for (int i = 0; i < n; i++) {
            View v = getChildAt(i);
            //Log.i("Aioros", "Child width = " + v.getWidth());
            allViewWidth += v.getWidth();
        }
        int blankWidth = (width - allViewWidth - paddingLeft - paddingRight) / (n - 1);
        //Log.i("Aioros", "blankWidth = " + blankWidth);

        LayoutParams params1 = (LayoutParams) viewList.get(1).getLayoutParams();
        params1.leftMargin = blankWidth;
        viewList.get(1).setLayoutParams(params1);

        LayoutParams params2 = (LayoutParams) viewList.get(2).getLayoutParams();
        params2.leftMargin = blankWidth;
        viewList.get(2).setLayoutParams(params2);

        LayoutParams params3 = (LayoutParams) viewList.get(3).getLayoutParams();
        params3.leftMargin = blankWidth;
        viewList.get(3).setLayoutParams(params3);
    }
}
