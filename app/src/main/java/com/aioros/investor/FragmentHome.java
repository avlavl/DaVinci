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
    private MessageAdapter mMsgAdapter;
    private List<MessageBean> mMsgBeanList = new ArrayList<MessageBean>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layoutHome = inflater.inflate(R.layout.home, container, false);
        Log.d(TAG, "onCreateView---->");
        mMainActivity = (MainActivity) getActivity();
        mFragmentManager = getActivity().getFragmentManager();
        mListView = (ListView) layoutHome.findViewById(R.id.listview_message);
        mMsgAdapter = new MessageAdapter(mMsgBeanList, mMainActivity);
        mListView.setAdapter(mMsgAdapter);
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(mMainActivity, mMsgBeanList.get(position).toString(), Toast.LENGTH_SHORT).show();
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
        mMsgBeanList.add(new MessageBean(R.mipmap.ic_photo_1, "张三", "吃饭没?", "昨天"));
        mMsgBeanList.add(new MessageBean(R.mipmap.ic_photo_2, "李四", "哈哈", "昨天"));
        mMsgBeanList.add(new MessageBean(R.mipmap.ic_photo_3, "小明", "吃饭没?", "昨天"));
        mMsgBeanList.add(new MessageBean(R.mipmap.ic_photo_4, "王五", "吃饭没?", "昨天"));
        mMsgBeanList.add(new MessageBean(R.mipmap.ic_photo_5, "Jack", "吃饭没?", "昨天"));
        mMsgBeanList.add(new MessageBean(R.mipmap.ic_photo_6, "Tome", "吃饭没?", "昨天"));
        mMsgBeanList.add(new MessageBean(R.mipmap.ic_photo_7, "Tony", "吃饭没?", "昨天"));
        mMsgBeanList.add(new MessageBean(R.mipmap.ic_photo_8, "Anna", "吃饭没?", "昨天"));
        mMsgBeanList.add(new MessageBean(R.mipmap.ic_photo_9, "Jone", "吃饭没?", "昨天"));
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
