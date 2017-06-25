package com.aioros.investor;

import java.util.ArrayList;

/**
 * Created by aizhang on 2017/6/25.
 */

public class TradeCheck {
    public ArrayList<Double> priceList;
    public FileUtility fileUtility;
    public Strategy strategy;
    public int rows;

    public TradeCheck(FileUtility fu) {
        fileUtility = fu;
        priceList = fu.closeList;
        rows = fu.rows;
    }

    public void sysMACDChk(BeanTradeMode tradeMode) {
        String[] ps = tradeMode.mModePara.split(",");
        int bp = Integer.parseInt(ps[0]);
//        CheckData chkData = new CheckData(mode, para0);
//        chkData.amount = paras[1];

        MACD macd = new MACD(priceList, 12, 26, 9);
        macd.init();
        strategy = new Strategy(priceList);
        strategy.macd = macd;

        for (int i = 0; i < rows; i++) {
            if (tradeMode.mModeName.equals("BAR")) {
                strategy.barCrossTrade(i, bp);
            } else {
                strategy.difCrossTrade(i, bp);
            }
        }
//        bpIndexList = strategy.bpIdxList;
//        spIndexList = strategy.spIdxList;
        if (strategy.bpIdxList.size() > strategy.spIdxList.size()) {
            tradeMode.mStatus = true;
            tradeMode.mDuration = TimeUtility.daysBetween(fileUtility.dateList, strategy.bpIdxList.get(strategy.bpIdxList.size() - 1), rows - 1);
//            chkData.status = "持有";
//            chkData.days = daysBetween(dateList, bpIndexList.get(bpIndexList.size() - 1), rows - 1);
        } else {
            tradeMode.mStatus = false;
            tradeMode.mDuration = TimeUtility.daysBetween(fileUtility.dateList, strategy.spIdxList.get(strategy.spIdxList.size() - 1), rows - 1);
//            chkData.status = "清空";
//            chkData.days = daysBetween(dateList, spIndexList.get(spIndexList.size() - 1), rows - 1);
        }

        tradeMode.mKeyPoint = macd.getMACDKey(tradeMode.mModeName, bp);
        tradeMode.mKeyRatio = 100 * (tradeMode.mKeyPoint - priceList.get(rows - 1)) / priceList.get(rows - 1);
//        chkData.key = macd.getMACDKey(mode, bp);
//        chkData.percent = 100 * (chkData.key - priceList.get(rows - 1)) / priceList.get(rows - 1);
//        chkDataList.add(chkData);
    }

    public void sysMAChk(BeanTradeMode tradeMode) {
        String[] ps = tradeMode.mModePara.split(",");
        int mas = Integer.parseInt(ps[0]);
        int mal = Integer.parseInt(ps[1]);
//        CheckData chkData = new CheckData("MA", para0);
//        chkData.amount = tradeMode.mAmount;

        MaLine ma = new MaLine(priceList);
        strategy = new Strategy(priceList);
        strategy.ma = ma;

        ArrayList<Double> masList = ma.getMAList(mas);
        ArrayList<Double> malList = ma.getMAList(mal);
        for (int i = 0; i < rows; i++) {
            strategy.maCrossTrade(i, masList, malList);
        }
//        bpIndexList = strategy.bpIdxList;
//        spIndexList = strategy.spIdxList;
        if (strategy.bpIdxList.size() > strategy.spIdxList.size()) {
            tradeMode.mStatus = true;
            tradeMode.mDuration = TimeUtility.daysBetween(fileUtility.dateList, strategy.bpIdxList.get(strategy.bpIdxList.size() - 1), rows - 1);
//            chkData.status = "持有";
//            chkData.days = daysBetween(dateList, bpIndexList.get(bpIndexList.size() - 1), rows - 1);
        } else {
            tradeMode.mStatus = false;
            tradeMode.mDuration = TimeUtility.daysBetween(fileUtility.dateList, strategy.spIdxList.get(strategy.spIdxList.size() - 1), rows - 1);
//            chkData.status = "清空";
//            chkData.days = daysBetween(dateList, spIndexList.get(spIndexList.size() - 1), rows - 1);
        }

        tradeMode.mKeyPoint = ma.getMAKey(mas, mal);
        tradeMode.mKeyRatio = 100 * (tradeMode.mKeyPoint - priceList.get(rows - 1)) / priceList.get(rows - 1);
//        chkData.key = ma.getMAKey(mas, mal);
//        chkData.percent = 100 * (chkData.key - priceList.get(rows - 1)) / priceList.get(rows - 1);
//        chkDataList.add(chkData);
    }

    public void sysLMChk(BeanTradeMode tradeMode) {
        String[] ps = tradeMode.mModePara.split(",");
        int t1 = Integer.parseInt(ps[0]);
        int t2 = Integer.parseInt(ps[1]);
//        CheckData chkData = new CheckData(mode, para0);
//        chkData.amount = paras[1];

        Livermore livermore = new Livermore(true, t1, t2);
        strategy = new Strategy(priceList);
        strategy.livermore = livermore;

        for (int i = 0; i < rows; i++) {
            livermore.arithmetic(priceList.get(i));
            if (tradeMode.mModeName.equals("LML")) {
                strategy.lmLongTrade(i);
            } else {
                strategy.lmShortTrade(i);
            }
        }
//        bpIndexList = strategy.bpIdxList;
//        spIndexList = strategy.spIdxList;
        if (strategy.bpIdxList.size() > strategy.spIdxList.size()) {
            tradeMode.mStatus = true;
            tradeMode.mDuration = TimeUtility.daysBetween(fileUtility.dateList, strategy.bpIdxList.get(strategy.bpIdxList.size() - 1), rows - 1);
//            chkData.status = "持有";
//            chkData.days = daysBetween(dateList, bpIndexList.get(bpIndexList.size() - 1), rows - 1);
        } else {
            tradeMode.mStatus = false;
            tradeMode.mDuration = TimeUtility.daysBetween(fileUtility.dateList, strategy.spIdxList.get(strategy.spIdxList.size() - 1), rows - 1);
//            chkData.status = "清空";
//            chkData.days = daysBetween(dateList, spIndexList.get(spIndexList.size() - 1), rows - 1);
        }

        tradeMode.mKeyPoint = livermore.getLMKey(tradeMode.mModeName);
        tradeMode.mKeyRatio = 100 * (tradeMode.mKeyPoint - priceList.get(rows - 1)) / priceList.get(rows - 1);
//        chkData.key = livermore.getLMKey(mode);
//        chkData.percent = 100 * (chkData.key - priceList.get(rows - 1)) / priceList.get(rows - 1);
//        chkDataList.add(chkData);
    }

    public void sysMACD2Chk(BeanTradeMode tradeMode) {
        String[] ps = tradeMode.mModePara.split(",");
        int bp0 = Integer.parseInt(ps[0]);
        int bp1 = Integer.parseInt(ps[1]);
//        CheckData chkData = new CheckData("BARDIF", para0);
//        chkData.amount = paras[1];

        MACD macd = new MACD(priceList, 12, 26, 9);
        macd.init();
        strategy = new Strategy(priceList);
        strategy.macd = macd;

        for (int i = 0; i < rows; i++) {
            strategy.barDifCrossTrade(i, bp0, bp1);
        }
//        bpIndexList = strategy.bpIdxList;
//        spIndexList = strategy.spIdxList;
        if (strategy.bpIdxList.size() > strategy.spIdxList.size()) {
            tradeMode.mStatus = true;
            tradeMode.mDuration = TimeUtility.daysBetween(fileUtility.dateList, strategy.bpIdxList.get(strategy.bpIdxList.size() - 1), rows - 1);
//            chkData.status = "持有";
//            chkData.days = daysBetween(dateList, bpIndexList.get(bpIndexList.size() - 1), rows - 1);
        } else {
            tradeMode.mStatus = false;
            tradeMode.mDuration = TimeUtility.daysBetween(fileUtility.dateList, strategy.spIdxList.get(strategy.spIdxList.size() - 1), rows - 1);
//            chkData.status = "清空";
//            chkData.days = daysBetween(dateList, spIndexList.get(spIndexList.size() - 1), rows - 1);
        }

        double barKey = macd.getBARKey(bp0);
        double difKey = macd.getDIFKey(bp1);
        tradeMode.mKeyPoint = (barKey > difKey) ? barKey : difKey;
        tradeMode.mKeyRatio = 100 * (tradeMode.mKeyPoint - priceList.get(rows - 1)) / priceList.get(rows - 1);
//        chkData.key = (barKey > difKey) ? barKey : difKey;
//        chkData.percent = 100 * (chkData.key - priceList.get(rows - 1)) / priceList.get(rows - 1);
//        chkDataList.add(chkData);
    }

    public void sysMACDMAChk(BeanTradeMode tradeMode) {
        String[] ps = tradeMode.mModePara.split(",");
        int bp = Integer.parseInt(ps[0]);
        int mas = Integer.parseInt(ps[1]);
        int mal = Integer.parseInt(ps[2]);
//        CheckData chkData = new CheckData(mode, para0);
//        chkData.amount = paras[1];

        MACD macd = new MACD(priceList, 12, 26, 9);
        macd.init();
        MaLine ma = new MaLine(priceList);
        strategy = new Strategy(priceList);
        strategy.macd = macd;
        strategy.ma = ma;

        ArrayList<Double> masList = ma.getMAList(mas);
        ArrayList<Double> malList = ma.getMAList(mal);
        for (int i = 0; i < rows; i++) {
            if (tradeMode.mModeName.equals("BARMA")) {
                strategy.barMACrossTrade(i, bp, masList, malList);
            } else {
                strategy.difMACrossTrade(i, bp, masList, malList);
            }
        }
//        bpIndexList = strategy.bpIdxList;
//        spIndexList = strategy.spIdxList;
        if (strategy.bpIdxList.size() > strategy.spIdxList.size()) {
            tradeMode.mStatus = true;
            tradeMode.mDuration = TimeUtility.daysBetween(fileUtility.dateList, strategy.bpIdxList.get(strategy.bpIdxList.size() - 1), rows - 1);
//            chkData.status = "持有";
//            chkData.days = daysBetween(dateList, bpIndexList.get(bpIndexList.size() - 1), rows - 1);
        } else {
            tradeMode.mStatus = false;
            tradeMode.mDuration = TimeUtility.daysBetween(fileUtility.dateList, strategy.spIdxList.get(strategy.spIdxList.size() - 1), rows - 1);
//            chkData.status = "清空";
//            chkData.days = daysBetween(dateList, spIndexList.get(spIndexList.size() - 1), rows - 1);
        }

        double maKey = ma.getMAKey(mas, mal);
        double macdKey = macd.getMACDKey(tradeMode.mModeName, bp);
        tradeMode.mKeyPoint = (maKey > macdKey) ? maKey : macdKey;
        tradeMode.mKeyRatio = 100 * (tradeMode.mKeyPoint - priceList.get(rows - 1)) / priceList.get(rows - 1);
//        chkData.key = (maKey > macdKey) ? maKey : macdKey;
//        chkData.percent = 100 * (chkData.key - priceList.get(rows - 1)) / priceList.get(rows - 1);
//        chkDataList.add(chkData);
    }

    public void sysMACDLMChk(BeanTradeMode tradeMode) {
        String[] ps = tradeMode.mModePara.split(",");
        int bp = Integer.parseInt(ps[0]);
        int t1 = Integer.parseInt(ps[1]);
        int t2 = Integer.parseInt(ps[2]);
//        CheckData chkData = new CheckData(mode, para0);
//        chkData.amount = paras[1];

        MACD macd = new MACD(priceList, 12, 26, 9);
        macd.init();
        Livermore livermore = new Livermore(true, t1, t2);
        strategy = new Strategy(priceList);
        strategy.macd = macd;
        strategy.livermore = livermore;

        for (int i = 0; i < rows; i++) {
            livermore.arithmetic(priceList.get(i));
            if (tradeMode.mModeName.equals("BARLML")) {
                strategy.barLMLCrossTrade(i, bp);
            } else if (tradeMode.mModeName.equals("BARLMS")) {
                strategy.barLMSCrossTrade(i, bp);
            } else if (tradeMode.mModeName.equals("DIFLML")) {
                strategy.difLMLCrossTrade(i, bp);
            } else if (tradeMode.mModeName.equals("DIFLMS")) {
                strategy.difLMSCrossTrade(i, bp);
            }
        }
//        bpIndexList = strategy.bpIdxList;
//        spIndexList = strategy.spIdxList;
        if (strategy.bpIdxList.size() > strategy.spIdxList.size()) {
            tradeMode.mStatus = true;
            tradeMode.mDuration = TimeUtility.daysBetween(fileUtility.dateList, strategy.bpIdxList.get(strategy.bpIdxList.size() - 1), rows - 1);
//            chkData.status = "持有";
//            chkData.days = daysBetween(dateList, bpIndexList.get(bpIndexList.size() - 1), rows - 1);
        } else {
            tradeMode.mStatus = false;
            tradeMode.mDuration = TimeUtility.daysBetween(fileUtility.dateList, strategy.spIdxList.get(strategy.spIdxList.size() - 1), rows - 1);
//            chkData.status = "清空";
//            chkData.days = daysBetween(dateList, spIndexList.get(spIndexList.size() - 1), rows - 1);
        }

        double lmKey = livermore.getLMKey(tradeMode.mModeName);
        double macdKey = macd.getMACDKey(tradeMode.mModeName, bp);
        tradeMode.mKeyPoint = (lmKey > macdKey) ? lmKey : macdKey;
        tradeMode.mKeyRatio = 100 * (tradeMode.mKeyPoint - priceList.get(rows - 1)) / priceList.get(rows - 1);
//        chkData.key = (lmKey > macdKey) ? lmKey : macdKey;
//        chkData.percent = 100 * (chkData.key - priceList.get(rows - 1)) / priceList.get(rows - 1);
//        chkDataList.add(chkData);
    }

}
