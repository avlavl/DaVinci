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
    public static final String FRAGMENT_FLAG_MORE = "资讯";

    public static final int INDEX_SZZS = 0;
    public static final int INDEX_SZCZ = 1;
    public static final int INDEX_CYBZ = 2;
    public static final int INDEX_HSSB = 3;
    public static final int INDEX_ZZWB = 4;
    public static final int INDEX_QZYY = 5;
    public static final int INDEX_ZZJG = 6;
    public static final int INDEX_SWZQ = 7;
    public static final int INDEX_YLCY = 8;
    public static final int INDEX_ZZCM = 9;
    public static final int INDEX_ZGHL = 10;
    public static final int INDEX_METF = 11;

    public static final int INDEX_TRADE_ZGHL = 5;

    public static final int NUMBER_STOCK = 11;
    public static final int NUMBER_METF = 9;
    public static final int NUMBER_TRADE_ITEM = 5;
    public static final int NUMBER_INVEST_ITEM = 3;

    public static final String STOCK_CODE_STR = "s_sh000001,s_sz399001,s_sz399006,s_sh000300,s_sh000905,s_sh000991,s_sz399967,s_sz399707,s_sz399812,s_sz399971,s_sz164906,";
    public static final String METF_CODE_STR = "s_sh511990,s_sh511880,s_sh511660,s_sh511810,s_sh511690,s_sh511900,s_sh511830,s_sh511980,s_sh511650";

    public static final String[] HOME_STOCK_NAMES = new String[]{"上证指数", "深证成指", "创业板指", "沪深300", "中证500", "全指医药", "中证军工", "申万证券", "养老产业", "中证传媒", "中国互联"};
    public static final String[] HOME_STOCK_CODES = new String[]{"000001", "399001", "399006", "000300", "000905", "000991", "399967", "399707", "399812", "399971", "164906"};
    public static final String[] CHANCE_METF_NAMES = new String[]{"华宝添益", "银华日利", "建信添益", "理财金H", "交易货币", "富国货币", "华泰货币", "现金添富", "华夏快线"};
    public static final String[] CHANCE_METF_CODES = new String[]{"511990", "511880", "511660", "511810", "511690", "511900", "511830", "511980", "511650"};
    public static final String[] TRADE_FILE_NAMES = new String[]{"全指医药", "中证军工", "创业板指", "申万证券", "沪深300"};
    public static final String[] TRADE_FILE_CODES = new String[]{"h000991", "z399967", "z399006", "z399707", "h000300"};

    public static final String[][] STOCK_PARA_ARRAY = {
            {       // 全指医药
                    "DIFMA -14,1,37 35000C", "MA 10,176 25000A",
                    "DIFLMS 20,18,2 17000b", "BARLML 65,7,2 3000c"
            },
            {       // 中证军工
                    "MA 1,14 40000C", "BARLML -76,8,4 20000B",
                    "BARLMS -50,17,2 12000C", "BARLML 97,18,8 8000c"
            },
            {       // 申万证券
                    "DIFMA -80,2,19 15000C", "DIFMA -14,9,130 15000B",
                    "BARMA 14,1,40 15000c", "BARLMS 8,13,3 15000c"
            },
            {       // 沪深300
                    "BARMA -11,2,115 20000C", "DIFMA -36,1,119 20000B",
                    "BARLMS -27,17,3 10000b", "DIFLMS -44,17,3 10000a"
            },
            {       // 创业板指
                    "DIFMA -42,7,15 15000C", "BARLML -55,7,4 15000A",
                    "BARLMS -62,8,4 15000b", "DIFMA 63,3,18 5000b"
            }
//            {       // 中国互联
//                    "BARMA -10,10,23 15000B", "BARDIF -7,-17 25000B", "DIFLMS 2,6,2 10000a"
//            }
    };
}
