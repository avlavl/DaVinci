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
    public static final int INDEX_HSSB = 1;
    public static final int INDEX_ZZWB = 2;
    public static final int INDEX_CYBZ = 3;
    public static final int INDEX_QZYY = 4;
    public static final int INDEX_ZZJG = 5;
    public static final int INDEX_SWZQ = 6;
    public static final int INDEX_YLCY = 7;
    public static final int INDEX_ZZCM = 8;
    public static final int INDEX_ZGHL = 9;
    public static final int INDEX_METF = 10;

    public static final int INDEX_TRADE_ZGHL = 3;

    public static final int NUMBER_STOCK = 10;
    public static final int NUMBER_METF = 9;
    public static final int NUMBER_TRADE_ITEM = 4;
    public static final int NUMBER_INVEST_ITEM = 3;

    public static final String STOCK_CODE_STR = "s_sh000001,s_sh000300,s_sh000905,s_sz399006,s_sh000991,s_sz399967,s_sz399707,s_sz399812,s_sz399971,s_sz164906,";
    public static final String METF_CODE_STR = "s_sh511990,s_sh511880,s_sh511660,s_sh511810,s_sh511690,s_sh511900,s_sh511830,s_sh511980,s_sh511650";

    public static final String[] HOME_STOCK_NAMES = new String[]{"上证指数", "沪深300", "中证500", "创业板指", "全指医药", "中证军工", "申万证券", "养老产业", "中证传媒", "中国互联"};
    public static final String[] HOME_STOCK_CODES = new String[]{"000001", "000300", "000905", "399006", "000991", "399967", "399707", "399812", "399971", "164906"};
    public static final String[] CHANCE_METF_NAMES = new String[]{"华宝添益", "银华日利", "建信添益", "理财金H", "交易货币", "富国货币", "华泰货币", "现金添富", "华夏快线"};
    public static final String[] CHANCE_METF_CODES = new String[]{"511990", "511880", "511660", "511810", "511690", "511900", "511830", "511980", "511650"};
    public static final String[] TRADE_FILE_NAMES = new String[]{"全指医药", "中证军工", "创业板指", "中国互联", "沪深300"};
    public static final String[] TRADE_FILE_CODES = new String[]{"h000991", "z399967", "z399006", "z164906", "h000300"};

    public static final String[][] STOCK_PARA_ARRAY = {
            {       // 全指医药
                    "DIFMA -14,1,37 35000C", "MA 10,176 25000A",
                    "DIFLMS 20,18,2 17000b", "BARLML 65,7,2 3000c"
            },
            {       // 中证军工
                    "MA 1,14 40000C", "BARLML -76,8,4 20000B",
                    "BARLMS -50,17,2 12000C", "BARLML 97,18,8 8000c"
            },
            {       // 创业板指
                    "DIFMA -42,7,15 15000C", "BARLML -55,7,4 15000A",
                    "BARLMS -62,8,4 15000b", "DIFMA 63,3,18 5000b"
            },
            {       // 中国互联
                    "BARMA -10,10,23 15000B", "BARDIF -7,-17 25000B", "DIFLMS 2,6,2 10000a"
            }
    };
}
