package com.aioros.investor;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate------");
        mMainActivity = (MainActivity) getActivity();
        mFuturesDatas = mMainActivity.mFuturesDatas;
        for (int i = 0; i < NUMBER_CHANCE; i++) {
            mBeanStockList.add(new BeanStock(mFuturesDatas[i][0], mFuturesDatas[i][0], mFuturesDatas[i][1], mFuturesDatas[i][2], mFuturesDatas[i][3] + "%"));
        }

        // 在主线程中声明一个消息处理对象Handler
        mMainActivity.mChanceHandler = new Handler() {
            // 重载消息处理方法，用于接收和处理WorkerThread发送的消息
            @Override
            public void handleMessage(Message msg) {
                mFuturesDatas = (String[][]) msg.obj;
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
        double value = Double.parseDouble(mBeanStockList.get(position).mStockValue);
        double yield = 0;
        if (value < 100) {
            yield = 10000 * (100 - value) / value;
            Toast.makeText(mMainActivity, String.format("预购收益：万%.1f  ", yield) + ((yield > 10) ? "买入" : "等待"), Toast.LENGTH_SHORT).show();
        } else {
            yield = 100 * (value - 100);
            Toast.makeText(mMainActivity, String.format("预沽收益：万%.1f  ", yield) + ((yield > 10) ? "卖出" : "等待"), Toast.LENGTH_SHORT).show();
        }
    }
}
