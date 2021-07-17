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
    public static final int INDEX_ZZJ = 7;
    public static final int INDEX_SWZQ = 8;
    public static final int INDEX_YLCY = 9;
    public static final int INDEX_YYYB = 10;
    public static final int INDEX_GZMT = 11;

    public static final int NUMBER_HOME = 15;
    public static final int NUMBER_TRADE = 6;
    public static final int NUMBER_INVEST = 4;
    public static final int NUMBER_CHANCE = 5;

    public static final String HOME_CODE_STR = "s_sh000001,s_sz399001,s_sz399006,s_sh000300,s_sh000905,s_sh000991,s_sz399967,s_sz399987,s_sz399707,s_sz399812,s_sh000978,s_sh600519,s_sh511990,s_sh511880,s_sh511660";

    public static final String[] HOME_NAMES = new String[]{"上证指数", "深证成指", "创业板指", "沪深300", "中证500", "全指医药", "中证军工", "中证酒", "申万证券", "养老产业", "医药100", "贵州茅台", "华宝添益", "银华日利", "建信添益"};
    public static final String[] HOME_CODES = new String[]{"000001", "399001", "399006", "000300", "000905", "000991", "399967", "399987", "399707", "399812", "000978", "600519", "511990", "511880", "511660"};
    public static final String[] TRADE_NAMES = new String[]{"沪深300", "中证500", "创业板指", "中证军工", "中证酒", "贵州茅台"};
    public static final String[] TRADE_BASES = new String[]{"沪深300", "中证500", "创业板指", "中证军工", "中证酒", "贵州茅台"};
    public static final int[] TRADE_IDX_BASE = new int[]{INDEX_HSSB, INDEX_ZZWB, INDEX_CYBZ, INDEX_ZZJG, INDEX_ZZJ, INDEX_GZMT};
    public static final int[] TRADE_IDX_SELF = new int[]{INDEX_HSSB, INDEX_ZZWB, INDEX_CYBZ, INDEX_ZZJG, INDEX_ZZJ, INDEX_GZMT};
    public static final String[] TRADE_CODES = new String[]{"h000300", "h000905", "z399006", "z399967", "z399987", "h600519"};
    public static final String[] INVEST_NAMES = new String[]{"申万证券", "养老产业", "医药100", "中证500"};
    public static final String[] INVEST_CODES = new String[]{"z399707", "z399812", "h000978", "h000905"};

    public static final String[][] STOCK_PARA_ARRAY = {
            {       // 沪深300
                    "BARMA -27,2,115 35000", "MA 4,151 35000",
                    "DIFLMS 21,17,3 15000", "DIFLMS -36,18,3 15000"
            },
            {       // 中证500
                    "BARMA 2,3,13 8000", "BARLML -10,8,2 8000",
                    "DIFMA 139,2,14 8000", "BARMA 77,10,118 6000"
            },
            {       // 创业板指
                    "DIFMA -61,7,15 25000", "DIFLML -33,12,2 35000",
                    "BARLMS -55,9,2 25000", "DIFMA 61,3,19 15000"
            },
            {       // 中证军工
                    "MA 1,14 50000", "BARLML -77,8,4 25000",
                    "BARLMS -50,17,3 15000", "BARLMS 24,15,7 10000"
            },
            {       // 中证酒
                    "DIFMA -73,3,10 35000", "MA 10,190 35000",
                    "DIF 86 20000", "BARDIF 34,86 10000"
            },
            {       // 贵州茅台
                    "MA 11,216 200000"
            }
    };
}
