package com.aioros.investor;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class FragmentHome extends BaseFragment {

    private static final String TAG = "FragmentHome";
    private MainActivity mMainActivity;
    private ListView mListView;
    private StockAdapter mMsgAdapter;
    private List<StockBean> stockBeanList = new ArrayList<StockBean>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layoutHome = inflater.inflate(R.layout.home, container, false);
        Log.d(TAG, "onCreateView---->");
        mMainActivity = (MainActivity) getActivity();
        mFragmentManager = getActivity().getFragmentManager();
        mListView = (ListView) layoutHome.findViewById(R.id.listview_message);
        mMsgAdapter = new StockAdapter(stockBeanList, mMainActivity);
        mListView.setAdapter(mMsgAdapter);
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(mMainActivity, stockBeanList.get(position).toString(), Toast.LENGTH_SHORT).show();
            }

        });
        return layoutHome;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e(TAG, "onAttach-----");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate------");
        stockBeanList.add(new StockBean(R.drawable.property_select, "上证指数", "000001", "3100.00", "0.20%", "61.00"));
        stockBeanList.add(new StockBean(R.drawable.fund_select, "深证成指", "399001", "3100.00", "0.20%", "61.00"));
        stockBeanList.add(new StockBean(R.drawable.news_select, "创业板指", "399006", "3100.00", "0.20%", "61.00"));
        stockBeanList.add(new StockBean(R.drawable.home_select, "沪深300", "000300", "3100.00", "0.20%", "61.00"));
        stockBeanList.add(new StockBean(R.drawable.trade_select, "中证500", "000905", "3100.00", "0.20%", "61.00"));
        stockBeanList.add(new StockBean(R.drawable.market_select, "腾讯济安", "000847", "3100.00", "0.20%", "61.00"));
        stockBeanList.add(new StockBean(R.drawable.material_select, "养老产业", "399812", "3100.00", "0.20%", "61.00"));
        stockBeanList.add(new StockBean(R.drawable.news_select, "医药100", "000978", "3100.00", "0.20%", "61.00"));
        stockBeanList.add(new StockBean(R.drawable.notice_select, "CSSW证券", "399707", "3100.00", "0.20%", "61.00"));
        stockBeanList.add(new StockBean(R.drawable.optional_select, "中证军工", "399967", "3100.00", "0.20%", "61.00"));
        stockBeanList.add(new StockBean(R.drawable.trade_select, "中证环保", "000827", "3100.00", "0.20%", "61.00"));
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
        MainActivity.currFragTag = Constant.FRAGMENT_FLAG_HOME;
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
}
