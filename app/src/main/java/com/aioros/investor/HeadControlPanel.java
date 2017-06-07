package com.aioros.investor;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HeadControlPanel extends RelativeLayout {

    private Context mContext;
    private TextView mMidleTitle;
    private TextView mRightTitle;
    private static final float middle_title_size = 20f;
    private static final float right_title_size = 17f;

    public HeadControlPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        mMidleTitle = (TextView) findViewById(R.id.midle_title);
        mRightTitle = (TextView) findViewById(R.id.right_title);
    }

    public void initHeadPanel() {
        if (mMidleTitle != null) {
            setMiddleTitle(Constant.FRAGMENT_FLAG_HOME);
        }
        if (mRightTitle != null) {
            setRightTitle(Constant.FRAGMENT_FLAG_HOME);
        }
    }

    public void setMiddleTitle(String s) {
        mMidleTitle.setText(s);
        mMidleTitle.setTextSize(middle_title_size);
    }

    public void setRightTitle(String s) {
        mRightTitle.setText(s);
        mRightTitle.setTextSize(right_title_size);
    }
}
