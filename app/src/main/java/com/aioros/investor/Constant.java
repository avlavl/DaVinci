package com.aioros.investor;

public class Constant {
	//Btn的标识
	public static final int BTN_FLAG_HOME = 0x01;
	public static final int BTN_FLAG_TRADE = 0x01 << 1;
	public static final int BTN_FLAG_INVEST = 0x01 << 2;
	public static final int BTN_FLAG_CHANCE = 0x01 << 3;
	public static final int BTN_FLAG_OTHER = 0x01 << 4;

	//Fragment的标识
	public static final String FRAGMENT_FLAG_HOME = "主页";
	public static final String FRAGMENT_FLAG_TRADE = "交易";
	public static final String FRAGMENT_FLAG_INVEST = "定投";
	public static final String FRAGMENT_FLAG_CHANCE = "机会";
	public static final String FRAGMENT_FLAG_OTHER = "其他";
}
