package com.aioros.investor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentChance extends BaseFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View layoutChance = inflater.inflate(R.layout.fragment_chance, container, false);
		return layoutChance;
	}
	
	
	@Override
	public void onResume() {
		super.onResume();
		MainActivity.currFragTag = Constant.FRAGMENT_FLAG_CHANCE;
	}
}
