package com.aioros.investor;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ImageText extends LinearLayout {
    private Context mContext = null;
    private ImageView mImageView = null;
    private TextView mTextView = null;
    private final static int DEFAULT_IMAGE_WIDTH = 64;
    private final static int DEFAULT_IMAGE_HEIGHT = 64;
    private int CHECKED_COLOR = Color.rgb(240, 0, 0); //选中红色
    private int UNCHECKED_COLOR = Color.GRAY;   //自然灰色

    public ImageText(Context context) {
        super(context);
        mContext = context;
    }

    public ImageText(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View parentView = inflater.inflate(R.layout.image_text, this, true);
        mImageView = (ImageView) parentView.findViewById(R.id.image_iamge_text);
        mTextView = (TextView) parentView.findViewById(R.id.text_iamge_text);
    }

    public void setImage(int id) {
        if (mImageView != null) {
            mImageView.setImageResource(id);
            setImageSize(DEFAULT_IMAGE_WIDTH, DEFAULT_IMAGE_HEIGHT);
        }
    }

    public void setText(String s) {
        if (mTextView != null) {
            mTextView.setText(s);
            mTextView.setTextColor(UNCHECKED_COLOR);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    private void setImageSize(int w, int h) {
        if (mImageView != null) {
            ViewGroup.LayoutParams params = mImageView.getLayoutParams();
            params.width = w;
            params.height = h;
            mImageView.setLayoutParams(params);
        }
    }

    public void setChecked(int itemID) {
        if (mTextView != null) {
            mTextView.setTextColor(CHECKED_COLOR);
        }
        int checkDrawableId = -1;
        switch (itemID) {
            case Constant.BTN_FLAG_HOME:
                checkDrawableId = R.drawable.home_select;
                break;
            case Constant.BTN_FLAG_TRADE:
                checkDrawableId = R.drawable.trade_select;
                break;
            case Constant.BTN_FLAG_INVEST:
                checkDrawableId = R.drawable.fund_select;
                break;
            case Constant.BTN_FLAG_CHANCE:
                checkDrawableId = R.drawable.notice_select;
                break;
            case Constant.BTN_FLAG_OTHER:
                checkDrawableId = R.drawable.material_select;
                break;
            default:
                break;
        }
        if (mImageView != null) {
            mImageView.setImageResource(checkDrawableId);
        }
    }
}
