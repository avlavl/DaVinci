package com.aioros.investor;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;


public class BottomControlPanel extends RelativeLayout implements View.OnClickListener {
    private Context mContext;
    private ImageText mBtnHome = null;
    private ImageText mBtnTrade = null;
    private ImageText mBtnInvest = null;
    private ImageText mBtnChance = null;
    private ImageText mBtnMore = null;
    private BottomPanelCallback mBottomCallback = null;
    private List<ImageText> mImageTextList = new ArrayList<ImageText>();

    public interface BottomPanelCallback {
        public void onBottomPanelClick(int itemId);
    }

    public BottomControlPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        mBtnHome = (ImageText) findViewById(R.id.imagetext_home);
        mBtnTrade = (ImageText) findViewById(R.id.imagetext_trade);
        mBtnInvest = (ImageText) findViewById(R.id.imagetext_invest);
        mBtnChance = (ImageText) findViewById(R.id.imagetext_chance);
        mBtnMore = (ImageText) findViewById(R.id.imagetext_more);
        mImageTextList.add(mBtnHome);
        mImageTextList.add(mBtnTrade);
        mImageTextList.add(mBtnInvest);
        mImageTextList.add(mBtnChance);
        mImageTextList.add(mBtnMore);
    }

    public void initBottomPanel() {
        if (mBtnHome != null) {
            mBtnHome.setImage(R.drawable.home_normal);
            mBtnHome.setText("主页");
        }
        if (mBtnTrade != null) {
            mBtnTrade.setImage(R.drawable.trade_normal);
            mBtnTrade.setText("交易");
        }
        if (mBtnInvest != null) {
            mBtnInvest.setImage(R.drawable.property_normal);
            mBtnInvest.setText("定投");
        }
        if (mBtnChance != null) {
            mBtnChance.setImage(R.drawable.notice_normal);
            mBtnChance.setText("机会");
        }
        if (mBtnMore != null) {
            mBtnMore.setImage(R.drawable.material_normal);
            mBtnMore.setText("更多");
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
            case R.id.imagetext_home:
                index = Constant.BTN_FLAG_HOME;
                mBtnHome.setChecked(index);
                break;
            case R.id.imagetext_trade:
                index = Constant.BTN_FLAG_TRADE;
                mBtnTrade.setChecked(index);
                break;
            case R.id.imagetext_invest:
                index = Constant.BTN_FLAG_INVEST;
                mBtnInvest.setChecked(index);
                break;
            case R.id.imagetext_chance:
                index = Constant.BTN_FLAG_CHANCE;
                mBtnChance.setChecked(index);
                break;
            case R.id.imagetext_more:
                index = Constant.BTN_FLAG_MORE;
                mBtnMore.setChecked(index);
                break;
            default:
                break;
        }
        if (mBottomCallback != null) {
            mBottomCallback.onBottomPanelClick(index);
        }
    }

    public void defaultBtnChecked() {
        if (mBtnHome != null) {
            mBtnHome.setChecked(Constant.BTN_FLAG_HOME);
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

        LayoutParams params1 = (LayoutParams) mImageTextList.get(1).getLayoutParams();
        params1.leftMargin = blankWidth;
        mImageTextList.get(1).setLayoutParams(params1);

        LayoutParams params2 = (LayoutParams) mImageTextList.get(2).getLayoutParams();
        params2.leftMargin = blankWidth;
        mImageTextList.get(2).setLayoutParams(params2);

        LayoutParams params3 = (LayoutParams) mImageTextList.get(3).getLayoutParams();
        params3.leftMargin = blankWidth;
        mImageTextList.get(3).setLayoutParams(params3);
    }
}
