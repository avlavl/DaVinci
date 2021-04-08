package com.aioros.investor;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static com.aioros.investor.Constant.*;

/**
 * Created by aizhang on 2017/6/7.
 */

public class FragmentChance extends BaseFragment {
    private static final String TAG = "FragmentChance";
    private MainActivity mMainActivity;
    private ListView mListView;
    private AdapterListViewChance mAdapterListView;
    private List<BeanStock> mBeanStockList = new ArrayList<BeanStock>();
    public String[][] mFuturesDatas;
    public Handler mHandler;
    private TextView mTextViewPoint;
    private TextView mTextViewRatio;
    private TextView mTextViewScope;
    private TextView mTextViewGain;
    public String[] mFuturesTradeDates = {"2021/04/16", "2021/05/21", "2021/06/18", "2021/09/17"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate------");
        mMainActivity = (MainActivity) getActivity();
        mFuturesDatas = mMainActivity.mFuturesDatas;
        String currentDate = TimeUtility.getCurrentDate();
        for (int i = 0; i < NUMBER_CHANCE; i++) {
            int leftDays = ((i == 0) ? 1 : TimeUtility.daysBetween(currentDate, mFuturesTradeDates[i - 1]));
            mBeanStockList.add(new BeanStock(mFuturesDatas[i][0], mFuturesDatas[i][1], mFuturesDatas[i][2], mFuturesDatas[i][3] + "%", leftDays));
        }

        // 在主线程中声明一个消息处理对象Handler
        mMainActivity.mChanceHandler = new Handler() {
            // 重载消息处理方法，用于接收和处理WorkerThread发送的消息
            @Override
            public void handleMessage(Message msg) {
                mFuturesDatas = (String[][]) msg.obj;
                updateIndexData(mBeanStockList.get(1).mLeftDays == 0);
                for (int i = 0; i < NUMBER_CHANCE; i++) {
                    mBeanStockList.get(i).mStockValue = mFuturesDatas[i][1];
                    mBeanStockList.get(i).mStockScope = mFuturesDatas[i][2];
                    mBeanStockList.get(i).mStockRatio = mFuturesDatas[i][3] + "%";
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

        mTextViewPoint = (TextView) view.findViewById(R.id.textViewFuturesIndexPoint);
        mTextViewRatio = (TextView) view.findViewById(R.id.textViewFuturesIndexRatio);
        mTextViewScope = (TextView) view.findViewById(R.id.textViewFuturesIndexScope);
        mTextViewGain = (TextView) view.findViewById(R.id.textViewFuturesIndexGain);
        updateIndexData(true);

        mListView = (ListView) view.findViewById(R.id.listViewChance);
        mAdapterListView = new AdapterListViewChance(mMainActivity, mBeanStockList);
        mListView.setAdapter(mAdapterListView);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mListViewOnItemClick(parent, view, position, id);
            }

        });
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
        Toast.makeText(mMainActivity, "点我没用！", Toast.LENGTH_SHORT).show();
    }

    public void updateIndexData(boolean settlement) {
        int color = Color.DKGRAY;
        if (mFuturesDatas[0][1].contains("--")) {
            color = Color.DKGRAY;
        } else if (Double.parseDouble(mFuturesDatas[0][2]) > 0) {
            color = Color.rgb(240, 0, 0);
        } else if (Double.parseDouble(mFuturesDatas[0][2]) < 0) {
            color = Color.rgb(0, 200, 0);
        }

        mTextViewPoint.setText(mFuturesDatas[0][1]);
        mTextViewPoint.setTextColor(color);
        mTextViewScope.setText(mFuturesDatas[0][2]);
        mTextViewScope.setTextColor(color);
        mTextViewRatio.setText(mFuturesDatas[0][3] + "%");
        mTextViewRatio.setTextColor(color);
        mTextViewGain.setText(String.format("%.0f", 200 * Float.parseFloat(mFuturesDatas[0][2])));
        mTextViewGain.setTextColor(color);
    }
}
