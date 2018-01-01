package com.aioros.investor;

public class Constant {
    //Btn的标识
    public static final int BTN_FLAG_HOME = 0x01;
    public static final int BTN_FLAG_TRADE = 0x01 << 1;
    public static final int BTN_FLAG_INVEST = 0x01 << 2;
    public static final int BTN_FLAG_CHANCE = 0x01 << 3;
    public static final int BTN_FLAG_MORE = 0x01 << 4;

    //Fragment的标识
    public static final String FRAGMENT_FLAG_HOME = "主页";
    public static final String FRAGMENT_FLAG_TRADE = "交易";
    public static final String FRAGMENT_FLAG_INVEST = "定投";
    public static final String FRAGMENT_FLAG_CHANCE = "机会";
    public static final String FRAGMENT_FLAG_MORE = "更多";

    public static final String[][] STOCK_INI_ARRAY = {
            {       // 淘金100
                    "BARMA -2,9,45 15000", "BARLML -3,17,6 15000", "DIF -7 25000",
                    "BARLML -3,17,2 *10000", "DIFMA 91,1,30 *8000", "BARMA 33,9,45 *7000"
            },
            {       // 养老产业
                    "DIFMA -13,1,40 7000", "BARLML -14,17,2 8000", "MA 10,176 10000",
                    "DIFLMS -36,18,2 *6000", "DIFLMS 21,17,2 *5000", "BARLMS 9,17,3 *4000"
            },
            {       // 医药100
                    "MA 1,40 3000", "MA 3,206 5000", "DIFLML -41,5,2 5000",
                    "DIFLMS -36,18,2 *3000", "DIFLMS 21,17,2 *3000", "BARMA 77,4,45 *1000"
            },
            {       // 中国互联
                    "BARMA -13,8,25 15000", "BARDIF -7,-17 15000", "DIFLMS 2,6,2 *10000"
            },
            {       // 沪深300
                    "DIFMA -39,1,119 7000", "MA 4,151 8000", "DIFLMS -36,18,3 *6000",
                    "DIFLMS 21,17,3 *5000", "DIFMA 59,2,15 *4000"
            },
            {       // 中证500
                    "BARMA -80,1,15 10000", "BARLMS -13,13,2 *8000", "DIFMA 139,2,14 *6000",
                    "BARLML 92,15,7 *4000", "BARMA 96,8,101 *2000"
            },
            {       // 创业板指
                    "DIFMA -42,7,15 7000", "DIFLML -33,12,2 6000",
                    "BARLMS -62,8,4 *5000", "DIFMA 63,3,18 *2000"
            }
    };
}
