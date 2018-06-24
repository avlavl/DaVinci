package com.aioros.investor;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import static com.aioros.investor.Constant.*;


public class BaseFragment extends Fragment {
    private static final String TAG = "BaseFragment";
    protected FragmentManager mFragmentManager = null;
    protected FragmentTransaction mFragmentTransaction = null;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //activity = getActivity();
        Log.i(TAG, "onAttach...");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate...");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView...");
        //View v = inflater.inflate(R.layout.messages_layout, container, false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i(TAG, "onActivityCreated...");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        Log.i(TAG, "onStart...");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.i(TAG, "onResume...");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.i(TAG, "onPause...");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.i(TAG, "onStop...");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        Log.i(TAG, "onDestroyView...");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy...");
        super.onDestroy();
    }

    public static BaseFragment newInstance(Context context, String tag) {
        BaseFragment baseFragment = null;
        if (TextUtils.equals(tag, FRAGMENT_FLAG_HOME)) {
            baseFragment = new FragmentHome();
        } else if (TextUtils.equals(tag, FRAGMENT_FLAG_TRADE)) {
            baseFragment = new FragmentTrade();
        } else if (TextUtils.equals(tag, FRAGMENT_FLAG_INVEST)) {
            baseFragment = new FragmentInvest();
        } else if (TextUtils.equals(tag, FRAGMENT_FLAG_CHANCE)) {
            baseFragment = new FragmentChance();
        } else if (TextUtils.equals(tag, FRAGMENT_FLAG_MORE)) {
            baseFragment = new FragmentMore();
        }
        return baseFragment;
    }
}
