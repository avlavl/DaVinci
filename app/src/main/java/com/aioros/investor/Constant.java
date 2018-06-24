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

    public static final int INDEX_SZZS = 0;
    public static final int INDEX_CYBZ = 1;
    public static final int INDEX_HSSB = 2;
    public static final int INDEX_ZZWB = 3;
    public static final int INDEX_YYYB = 4;
    public static final int INDEX_SWZQ = 5;
    public static final int INDEX_YLCY = 6;
    public static final int INDEX_ZZCM = 7;
    public static final int INDEX_ZAHL = 8;
    public static final int INDEX_ZUHL = 9;
    public static final int INDEX_METF = 10;
    public static final int INDEX_TRADE_ZUHL = 2;

    public static final int NUMBER_STOCK = 10;
    public static final int NUMBER_METF = 9;
    public static final int NUMBER_TRADE_ITEM = 6;
    public static final int NUMBER_INVEST_ITEM = 3;

    public static final String[][] STOCK_PARA_ARRAY = {
            {       // 淘金100
                    "BARMA -2,9,45 15000C", "BARLML -3,17,6 15000C", "DIF -7 25000B",
                    "BARLML -3,17,2 10000c", "DIFMA 91,1,30 8000c", "BARMA 33,9,45 7000c"
            },
            {       // 医药100
                    "MA 1,40 3000C", "MA 3,206 5000A", "DIFLML -41,5,2 5000A",
                    "DIFLMS -36,18,2 3000a", "DIFLMS 21,17,2 3000b", "BARMA 77,4,45 1000c"
            },
            {       // 中国互联
                    "BARMA -13,8,25 15000B", "BARDIF -7,-17 15000B", "DIFLMS 2,6,2 10000a"
            },
            {       // 沪深300
                    "DIFMA -39,1,119 7000B", "MA 4,151 8000A", "DIFLMS -36,18,3 6000a",
                    "DIFLMS 21,17,3 5000a", "DIFMA 59,2,15 4000c"
            },
            {       // 中证500
                    "BARMA -80,1,15 10000C", "BARLMS -13,13,2 8000c", "DIFMA 139,2,14 6000c",
                    "BARLML 92,15,7 4000c", "BARMA 96,8,101 2000c"
            },
            {       // 创业板指
                    "DIFMA -42,7,15 7000C", "DIFLML -33,12,2 6000A",
                    "BARLMS -62,8,4 5000b", "DIFMA 63,3,18 2000b"
            }
    };
}
