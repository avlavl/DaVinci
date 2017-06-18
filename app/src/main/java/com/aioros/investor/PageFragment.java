package com.aioros.investor;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by aizhang on 2017/6/5.
 */

public class PageFragment extends Fragment {

    private int mPage;
    private String mName;
    public View view;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt("KEY_PAGE");
        mName = getArguments().getString("KEY_NAME");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment, container, false);
        Button button = (Button) view.findViewById(R.id.button_pager_chance);
        TextView textView = (TextView) view.findViewById(R.id.textview_pager_chance);
        button.setText("Fragment #" + mPage);
        textView.setText("股票名称：" + mName);
        return view;
    }

    public static PageFragment newInstance(int page, String name) {
        Bundle bundle = new Bundle();
        bundle.putInt("KEY_PAGE", page);
        bundle.putString("KEY_NAME", name);
        PageFragment pageFragment = new PageFragment();
        pageFragment.setArguments(bundle);
        return pageFragment;
    }
}
