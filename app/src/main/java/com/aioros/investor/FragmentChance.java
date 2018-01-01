package com.aioros.investor;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by aizhang on 2017/6/7.
 */

public class FragmentChance extends BaseFragment {
    private static final String TAG = "FragmentChance";
    private MainActivity mMainActivity;
    private String[] mStockNames = new String[]{"华宝添益", "银华日利", "建信添益", "理财金H", "交易货币", "富国货币", "华泰货币", "现金添富", "华夏快线"};
    private String[] mStockCodes = new String[]{"511990", "511880", "511660", "511810", "511690", "511900", "511830", "511980", "511650"};
    private ListView mListView;
    private AdapterListViewStock mAdapterListView;
    private List<BeanStock> mBeanStockList = new ArrayList<BeanStock>();
    public String[][] mMarketDatas;
    private int offset = 10;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate------");
        mMainActivity = (MainActivity) getActivity();
        mMarketDatas = mMainActivity.mMarketDatas;
        for (int i = 0; i < mStockNames.length; i++) {
            mBeanStockList.add(new BeanStock(mStockNames[i], mStockCodes[i], mMarketDatas[offset + i][1], mMarketDatas[offset + i][2], mMarketDatas[offset + i][3] + "%"));
        }

        // 在主线程中声明一个消息处理对象Handler
        mMainActivity.mChanceHandler = new Handler() {
            // 重载消息处理方法，用于接收和处理WorkerThread发送的消息
            @Override
            public void handleMessage(Message msg) {
                System.out.println("-------------mChanceHandler handle");
                mMarketDatas = (String[][]) msg.obj;
                for (int i = 0; i < mStockNames.length; i++) {
                    mBeanStockList.get(i).mStockValue = mMarketDatas[offset + i][1];
                    mBeanStockList.get(i).mStockScope = mMarketDatas[offset + i][2];
                    mBeanStockList.get(i).mStockRatio = mMarketDatas[offset + i][3] + "%";
                    double value = Double.parseDouble(mBeanStockList.get(i).mStockValue);
                    if (value < 99.97) {
                        NotificationManager notificationManager = (NotificationManager) mMainActivity.getSystemService(NOTIFICATION_SERVICE);
                        Notification.Builder builder = new Notification.Builder(mMainActivity);
                        builder.setSmallIcon(R.mipmap.ic_app_logo);
                        builder.setContentTitle(mStockNames[i]); //下拉通知栏标题
                        builder.setContentText(mStockNames[i] + "当前值" + value + "满足要求"); //下拉通知栏内容
                        builder.setAutoCancel(true);
                        builder.setSound(Uri.fromFile(new File("/system/media/audio/notifications/CrystalRing.ogg")));
                        builder.setDefaults(Notification.DEFAULT_VIBRATE);
                        Intent intent = new Intent(mMainActivity, MainActivity.class);
                        PendingIntent contentIntent = PendingIntent.getActivity(mMainActivity, 0, intent, 0);
                        builder.setContentIntent(contentIntent); //点击跳转的intent
                        Notification notification = builder.build();
                        notificationManager.notify((int) System.currentTimeMillis(), notification);
                    }
                }
                mAdapterListView.notifyDataSetChanged();
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chance, container, false);
        Log.d(TAG, "onCreateView---->");
        mFragmentManager = getActivity().getFragmentManager();
        mListView = (ListView) view.findViewById(R.id.listViewMETF);
        mAdapterListView = new AdapterListViewStock(mMainActivity, mBeanStockList);
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
        MainActivity.currFragTag = Constant.FRAGMENT_FLAG_CHANCE;
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
        Toast.makeText(mMainActivity, mBeanStockList.get(position).toString(), Toast.LENGTH_SHORT).show();
        mMainActivity.mBottomPanel.mBtnTrade.callOnClick();
    }
}
