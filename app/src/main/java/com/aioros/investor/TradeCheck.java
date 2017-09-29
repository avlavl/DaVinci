package com.aioros.investor;

import java.util.ArrayList;

/**
 * Created by aizhang on 2017/6/25.
 */

public class TradeCheck {
    public ArrayList<String> dateList;
    public ArrayList<Double> priceList;
    public ArrayList<Double> closeList;
    public int rows;
    public Strategy strategy;
    public ArrayList<Integer> bpIndexList = new ArrayList<>();
    public ArrayList<Integer> spIndexList = new ArrayList<>();

    public TradeCheck(FileUtility fu) {
        dateList = fu.dateList;
        priceList = fu.closeList;
        rows = fu.rows;
        closeList = (rows != fu.rows2) ? fu.closeList : fu.closeList2;
    }

    public void sysMACDChk(BeanTradeMode tradeMode) {
        String[] ps = tradeMode.mModePara.split(",");
        int bp = Integer.parseInt(ps[0]);

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
        bpIndexList = strategy.bpIdxList;
        spIndexList = strategy.spIdxList;
        if (bpIndexList.size() > spIndexList.size()) {
            tradeMode.mStatus = true;
            tradeMode.mDuration = TimeUtility.daysBetween(dateList, bpIndexList.get(bpIndexList.size() - 1), rows - 1);
            tradeMode.mCost = closeList.get(bpIndexList.get(bpIndexList.size() - 1));
        } else {
            tradeMode.mStatus = false;
            tradeMode.mDuration = TimeUtility.daysBetween(dateList, spIndexList.get(spIndexList.size() - 1), rows - 1);
            tradeMode.mCost = closeList.get(spIndexList.get(spIndexList.size() - 1));
        }

        tradeMode.mRatio = (closeList.get(rows - 1) - tradeMode.mCost) * 100 / tradeMode.mCost;
        tradeMode.mKeyPoint = macd.getMACDKey(tradeMode.mModeName, bp);
        tradeMode.mKeyRatio = 100 * (tradeMode.mKeyPoint - priceList.get(rows - 1)) / priceList.get(rows - 1);
    }

    public void sysMAChk(BeanTradeMode tradeMode) {
        String[] ps = tradeMode.mModePara.split(",");
        int mas = Integer.parseInt(ps[0]);
        int mal = Integer.parseInt(ps[1]);

        MaLine ma = new MaLine(priceList);
        strategy = new Strategy(priceList);
        strategy.ma = ma;

        ArrayList<Double> masList = ma.getMAList(mas);
        ArrayList<Double> malList = ma.getMAList(mal);
        for (int i = 0; i < rows; i++) {
            strategy.maCrossTrade(i, masList, malList);
        }
        bpIndexList = strategy.bpIdxList;
        spIndexList = strategy.spIdxList;
        if (bpIndexList.size() > spIndexList.size()) {
            tradeMode.mStatus = true;
            tradeMode.mDuration = TimeUtility.daysBetween(dateList, bpIndexList.get(bpIndexList.size() - 1), rows - 1);
            tradeMode.mCost = closeList.get(bpIndexList.get(bpIndexList.size() - 1));
        } else {
            tradeMode.mStatus = false;
            tradeMode.mDuration = TimeUtility.daysBetween(dateList, spIndexList.get(spIndexList.size() - 1), rows - 1);
            tradeMode.mCost = closeList.get(spIndexList.get(spIndexList.size() - 1));
        }

        tradeMode.mRatio = (closeList.get(rows - 1) - tradeMode.mCost) * 100 / tradeMode.mCost;
        tradeMode.mKeyPoint = ma.getMAKey(mas, mal);
        tradeMode.mKeyRatio = 100 * (tradeMode.mKeyPoint - priceList.get(rows - 1)) / priceList.get(rows - 1);
    }

    public void sysLMChk(BeanTradeMode tradeMode) {
        String[] ps = tradeMode.mModePara.split(",");
        int t1 = Integer.parseInt(ps[0]);
        int t2 = Integer.parseInt(ps[1]);

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
        bpIndexList = strategy.bpIdxList;
        spIndexList = strategy.spIdxList;
        if (bpIndexList.size() > spIndexList.size()) {
            tradeMode.mStatus = true;
            tradeMode.mDuration = TimeUtility.daysBetween(dateList, bpIndexList.get(bpIndexList.size() - 1), rows - 1);
            tradeMode.mCost = closeList.get(bpIndexList.get(bpIndexList.size() - 1));
        } else {
            tradeMode.mStatus = false;
            tradeMode.mDuration = TimeUtility.daysBetween(dateList, spIndexList.get(spIndexList.size() - 1), rows - 1);
            tradeMode.mCost = closeList.get(spIndexList.get(spIndexList.size() - 1));
        }

        tradeMode.mRatio = (closeList.get(rows - 1) - tradeMode.mCost) * 100 / tradeMode.mCost;
        tradeMode.mKeyPoint = livermore.getLMKey(tradeMode.mModeName);
        tradeMode.mKeyRatio = 100 * (tradeMode.mKeyPoint - priceList.get(rows - 1)) / priceList.get(rows - 1);
    }

    public void sysMACD2Chk(BeanTradeMode tradeMode) {
        String[] ps = tradeMode.mModePara.split(",");
        int bp0 = Integer.parseInt(ps[0]);
        int bp1 = Integer.parseInt(ps[1]);

        MACD macd = new MACD(priceList, 12, 26, 9);
        macd.init();
        strategy = new Strategy(priceList);
        strategy.macd = macd;

        for (int i = 0; i < rows; i++) {
            strategy.barDifCrossTrade(i, bp0, bp1);
        }
        bpIndexList = strategy.bpIdxList;
        spIndexList = strategy.spIdxList;
        if (bpIndexList.size() > spIndexList.size()) {
            tradeMode.mStatus = true;
            tradeMode.mDuration = TimeUtility.daysBetween(dateList, bpIndexList.get(bpIndexList.size() - 1), rows - 1);
            tradeMode.mCost = closeList.get(bpIndexList.get(bpIndexList.size() - 1));
        } else {
            tradeMode.mStatus = false;
            tradeMode.mDuration = TimeUtility.daysBetween(dateList, spIndexList.get(spIndexList.size() - 1), rows - 1);
            tradeMode.mCost = closeList.get(spIndexList.get(spIndexList.size() - 1));
        }

        tradeMode.mRatio = (closeList.get(rows - 1) - tradeMode.mCost) * 100 / tradeMode.mCost;
        double barKey = macd.getBARKey(bp0);
        double difKey = macd.getDIFKey(bp1);
        tradeMode.mKeyPoint = (barKey > difKey) ? barKey : difKey;
        tradeMode.mKeyRatio = 100 * (tradeMode.mKeyPoint - priceList.get(rows - 1)) / priceList.get(rows - 1);
    }

    public void sysMACDMAChk(BeanTradeMode tradeMode) {
        String[] ps = tradeMode.mModePara.split(",");
        int bp = Integer.parseInt(ps[0]);
        int mas = Integer.parseInt(ps[1]);
        int mal = Integer.parseInt(ps[2]);

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
        bpIndexList = strategy.bpIdxList;
        spIndexList = strategy.spIdxList;
        if (bpIndexList.size() > spIndexList.size()) {
            tradeMode.mStatus = true;
            tradeMode.mDuration = TimeUtility.daysBetween(dateList, bpIndexList.get(bpIndexList.size() - 1), rows - 1);
            tradeMode.mCost = closeList.get(bpIndexList.get(bpIndexList.size() - 1));
        } else {
            tradeMode.mStatus = false;
            tradeMode.mDuration = TimeUtility.daysBetween(dateList, spIndexList.get(spIndexList.size() - 1), rows - 1);
            tradeMode.mCost = closeList.get(spIndexList.get(spIndexList.size() - 1));
        }

        tradeMode.mRatio = (closeList.get(rows - 1) - tradeMode.mCost) * 100 / tradeMode.mCost;
        double maKey = ma.getMAKey(mas, mal);
        double macdKey = macd.getMACDKey(tradeMode.mModeName, bp);
        tradeMode.mKeyPoint = (maKey > macdKey) ? maKey : macdKey;
        tradeMode.mKeyRatio = 100 * (tradeMode.mKeyPoint - priceList.get(rows - 1)) / priceList.get(rows - 1);
    }

    public void sysMACDLMChk(BeanTradeMode tradeMode) {
        String[] ps = tradeMode.mModePara.split(",");
        int bp = Integer.parseInt(ps[0]);
        int t1 = Integer.parseInt(ps[1]);
        int t2 = Integer.parseInt(ps[2]);

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
        bpIndexList = strategy.bpIdxList;
        spIndexList = strategy.spIdxList;
        if (bpIndexList.size() > spIndexList.size()) {
            tradeMode.mStatus = true;
            tradeMode.mDuration = TimeUtility.daysBetween(dateList, bpIndexList.get(bpIndexList.size() - 1), rows - 1);
            tradeMode.mCost = closeList.get(bpIndexList.get(bpIndexList.size() - 1));
        } else {
            tradeMode.mStatus = false;
            tradeMode.mDuration = TimeUtility.daysBetween(dateList, spIndexList.get(spIndexList.size() - 1), rows - 1);
            tradeMode.mCost = closeList.get(spIndexList.get(spIndexList.size() - 1));
        }

        tradeMode.mRatio = (closeList.get(rows - 1) - tradeMode.mCost) * 100 / tradeMode.mCost;
        double lmKey = livermore.getLMKey(tradeMode.mModeName);
        double macdKey = macd.getMACDKey(tradeMode.mModeName, bp);
        tradeMode.mKeyPoint = (lmKey > macdKey) ? lmKey : macdKey;
        tradeMode.mKeyRatio = 100 * (tradeMode.mKeyPoint - priceList.get(rows - 1)) / priceList.get(rows - 1);
    }

}
