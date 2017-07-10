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
                    "BARLML -3,17,6 7000", "BARDIF -3,-43 7000", "BARMA -3,9,45 6000",
                    "DIFLML -80,5,2 20000", "DIFMA 91,1,30 *10000", "LMS 10,2 *10000"
            },
            {       // 腾讯济安
                    "BARMA -72,5,50 15000", "BARLMS -72,9,2 15000", "DIFLMS -5,13,2 10000",
                    "LMS 12,2 *8000", "DIFLMS 45,12,2 *6000", "BARDIF 10,97 *6000"
            },
            {       // 养老产业
                    "DIFMA -13,1,40 10000", "DIFMA -39,4,190 15000", "MA 4,143 15000",
                    "DIFMA 36,10,220 *8000", "DIFLMS -36,18,2 *8000", "BARLMS 9,10,2 *4000"
            },
            {       // 医药100
                    "DIFMA -43,1,37 4000", "MA 1,40 4000", "DIFMA -58,4,151 4000",
                    "MA 5,232 4000", "DIFLML -41,5,2 4000", "BARMA 77,10,25 *4000"
            },
            {       // 沪深300
                    "BARMA -16,2,40 5000", "DIFMA -36,1,119 5000", "MA 4,151 5000",
                    "DIFLMS -44,17,3 5000", "DIFLMS 20,17,3 *5000", "BARMA 92,6,69 *5000"
            },
            {       // 中证500
                    "BARMA -80,1,15 6000", "BARLML -8,8,2 6000", "BARLMS -13,13,2 6000",
                    "DIFMA 139,2,14 *4000", "BARLML 92,15,7 *4000", "BARMA 95,8,105 *4000"
            },
            {       // 创业板指
                    "DIFMA -23,4,17 5000", "BARLMS -62,8,4 10000", "BARLML -62,12,2 5000",
                    "DIFMA 63,3,18 *4000", "BARDIF -34,61 *4000", "BARLML 73,6,3 *2000"
            }
    };
}
