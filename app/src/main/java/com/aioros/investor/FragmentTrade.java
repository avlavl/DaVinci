package com.aioros.investor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentTrade extends BaseFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View layoutTrade = inflater.inflate(R.layout.trade, container, false);
		return layoutTrade;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		MainActivity.currFragTag = Constant.FRAGMENT_FLAG_TRADE;
	}
}
