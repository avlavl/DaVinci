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
    public static final int INDEX_YYYB = 9;
    public static final int INDEX_GZMT = 10;
    public static final int INDEX_STOCK = 11;

    public static final int NUMBER_HOME = 11;
    public static final int NUMBER_TRADE = 5;
    public static final int NUMBER_INVEST = 4;
    public static final int NUMBER_CHANCE = 5;

    public static final String HOME_CODE_STR = "s_sh000001,s_sz399001,s_sz399006,s_sh000300,s_sh000905,s_sh000991,s_sz399967,s_sz399707,s_sz399812,s_sh000978,s_sh600519";
    public static final String CHANCE_CODE_STR = ",s_sh511990,s_sh511880,s_sh511660,s_sh511810,s_sh511690";

    public static final String[] HOME_NAMES = new String[]{"上证指数", "深证成指", "创业板指", "沪深300", "中证500", "全指医药", "中证军工", "申万证券", "养老产业", "医药100", "贵州茅台"};
    public static final String[] HOME_CODES = new String[]{"000001", "399001", "399006", "000300", "000905", "000991", "399967", "399707", "399812", "000978", "600519"};
    public static final String[] TRADE_NAMES = new String[]{"全指医药", "中证军工", "申万证券", "沪深300", "贵州茅台"};
    public static final String[] TRADE_CODES = new String[]{"h000991", "z399967", "z399707", "h000300", "h600519"};
    public static final String[] INVEST_NAMES = new String[]{"申万证券", "养老产业", "医药100", "中证500"};
    public static final String[] INVEST_CODES = new String[]{"z399707", "z399812", "h000978", "h000905"};
    public static final String[] CHANCE_NAMES = new String[]{"华宝添益", "银华日利", "建信添益", "理财金H", "交易货币"};
    public static final String[] CHANCE_CODES = new String[]{"511990", "511880", "511660", "511810", "511690"};

    public static final String[][] STOCK_PARA_ARRAY = {
            {       // 全指医药
                    "DIFMA -11,1,37 30000C", "MA 10,176 30000A",
                    "DIFLMS 20,18,2 16000b", "BARLML 56,7,2 4000c"
            },
            {       // 中证军工
                    "MA 1,14 40000C", "BARLML -77,8,4 20000B",
                    "BARLMS -50,17,2 12000C", "BARMA 74,10,144 8000c"
            },
            {       // 申万证券
                    "DIFMA -80,2,19 22000C", "DIFMA -14,9,130 20000B",
                    "BARLMS 8,13,3 12000c", "BARLMS 100,11,3 6000c"
            },
            {       // 沪深300
                    "BARMA -27,2,115 20000C", "MA 4,151 20000B",
                    "DIFLMS 21,17,3 10000b", "DIFLMS -36,18,3 10000a"
            },
            {       // 贵州茅台
                    "MA 9,200 100000A"
            }
    };
}
