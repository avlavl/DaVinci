package com.aioros.investor;

import java.util.ArrayList;

/**
 * Created by aizhang on 2017/6/25.
 */

public class TradeCheck {
    public ArrayList<String> dateList;
    public ArrayList<Double> closeList;
    public ArrayList<String> dateList2;
    public ArrayList<Double> closeList2;
    public ArrayList<Double> closeCheckList;
    public int rows, rows2;

    public StrategyTrade strategy;

    public TradeCheck(FileUtility fu, boolean self) {
        dateList = fu.dateList1;
        closeList = fu.closeList1;
        dateList2 = fu.dateList2;
        closeList2 = fu.closeList2;
        closeCheckList = self ? closeList : closeList2;
        rows = fu.rows1;
        rows2 = fu.rows2;
    }

    public void sysMACDChk(BeanTradeMode tradeMode) {
        String[] ps = tradeMode.mModePara.split(",");
        int bp = Integer.parseInt(ps[0]);

        MACD macd = new MACD(closeList, 12, 26, 9);
        macd.init();
        strategy = new StrategyTrade(closeList);
        strategy.macd = macd;

        for (int i = 0; i < rows; i++) {
            if (tradeMode.mModeName.equals("BAR")) {
                strategy.barCrossTrade(i, bp);
            } else {
                strategy.difCrossTrade(i, bp);
            }
        }
        tradeMode.bpIdxList = strategy.bpIdxList;
        tradeMode.spIdxList = strategy.spIdxList;
        if (tradeMode.bpIdxList.size() > tradeMode.spIdxList.size()) {
            tradeMode.mStatus = true;
            tradeMode.mDuration = TimeUtility.daysBetween(dateList.get(tradeMode.bpIdxList.get(tradeMode.bpIdxList.size() - 1)), TimeUtility.getCurrentDate());
            tradeMode.mBSPoint = closeCheckList.get(tradeMode.bpIdxList.get(tradeMode.bpIdxList.size() - 1));
            tradeMode.mMeanDate = (getMeanPositionDays(tradeMode.bpIdxList, tradeMode.spIdxList) * tradeMode.spIdxList.size() + tradeMode.mDuration) / tradeMode.bpIdxList.size();
            tradeMode.mPosRate = 100 * (getPositionDaysRate(tradeMode.bpIdxList, tradeMode.spIdxList) * dateList.size() + dateList.size() - tradeMode.bpIdxList.get(tradeMode.bpIdxList.size() - 1)) / dateList.size();
        } else {
            tradeMode.mStatus = false;
            tradeMode.mDuration = TimeUtility.daysBetween(dateList.get(tradeMode.spIdxList.get(tradeMode.spIdxList.size() - 1)), TimeUtility.getCurrentDate());
            tradeMode.mBSPoint = closeCheckList.get(tradeMode.spIdxList.get(tradeMode.spIdxList.size() - 1));
            tradeMode.mMeanDate = getMeanPositionDays(tradeMode.bpIdxList, tradeMode.spIdxList);
            tradeMode.mPosRate = 100 * getPositionDaysRate(tradeMode.bpIdxList, tradeMode.spIdxList);
        }

        tradeMode.mKeyPoint = macd.getMACDKey(tradeMode.mModeName, bp);
        tradeMode.mKeyRatio = 100 * (tradeMode.mKeyPoint - closeList.get(rows - 1)) / closeList.get(rows - 1);
    }

    public void sysMAChk(BeanTradeMode tradeMode) {
        String[] ps = tradeMode.mModePara.split(",");
        int mas = Integer.parseInt(ps[0]);
        int mal = Integer.parseInt(ps[1]);

        MAL ma = new MAL(closeList);
        strategy = new StrategyTrade(closeList);
        strategy.ma = ma;

        ArrayList<Double> masList = ma.getMAList(mas);
        ArrayList<Double> malList = ma.getMAList(mal);
        for (int i = 0; i < rows; i++) {
            strategy.maCrossTrade(i, masList, malList);
        }
        tradeMode.bpIdxList = strategy.bpIdxList;
        tradeMode.spIdxList = strategy.spIdxList;
        if (tradeMode.bpIdxList.size() > tradeMode.spIdxList.size()) {
            tradeMode.mStatus = true;
            tradeMode.mDuration = TimeUtility.daysBetween(dateList.get(tradeMode.bpIdxList.get(tradeMode.bpIdxList.size() - 1)), TimeUtility.getCurrentDate());
            tradeMode.mBSPoint = closeCheckList.get(tradeMode.bpIdxList.get(tradeMode.bpIdxList.size() - 1));
            tradeMode.mMeanDate = (getMeanPositionDays(tradeMode.bpIdxList, tradeMode.spIdxList) * tradeMode.spIdxList.size() + tradeMode.mDuration) / tradeMode.bpIdxList.size();
            tradeMode.mPosRate = 100 * (getPositionDaysRate(tradeMode.bpIdxList, tradeMode.spIdxList) * dateList.size() + dateList.size() - tradeMode.bpIdxList.get(tradeMode.bpIdxList.size() - 1)) / dateList.size();
        } else {
            tradeMode.mStatus = false;
            tradeMode.mDuration = TimeUtility.daysBetween(dateList.get(tradeMode.spIdxList.get(tradeMode.spIdxList.size() - 1)), TimeUtility.getCurrentDate());
            tradeMode.mBSPoint = closeCheckList.get(tradeMode.spIdxList.get(tradeMode.spIdxList.size() - 1));
            tradeMode.mMeanDate = getMeanPositionDays(tradeMode.bpIdxList, tradeMode.spIdxList);
            tradeMode.mPosRate = 100 * getPositionDaysRate(tradeMode.bpIdxList, tradeMode.spIdxList);
        }

        tradeMode.mKeyPoint = ma.getMAKey(mas, mal);
        tradeMode.mKeyRatio = 100 * (tradeMode.mKeyPoint - closeList.get(rows - 1)) / closeList.get(rows - 1);
    }

    public void sysLMChk(BeanTradeMode tradeMode) {
        String[] ps = tradeMode.mModePara.split(",");
        int t1 = Integer.parseInt(ps[0]);
        int t2 = Integer.parseInt(ps[1]);

        Livermore livermore = new Livermore(true, t1, t2);
        strategy = new StrategyTrade(closeList);
        strategy.livermore = livermore;

        for (int i = 0; i < rows; i++) {
            livermore.arithmetic(closeList.get(i));
            if (tradeMode.mModeName.equals("LML")) {
                strategy.lmLongTrade(i);
            } else {
                strategy.lmShortTrade(i);
            }
        }
        tradeMode.bpIdxList = strategy.bpIdxList;
        tradeMode.spIdxList = strategy.spIdxList;
        if (tradeMode.bpIdxList.size() > tradeMode.spIdxList.size()) {
            tradeMode.mStatus = true;
            tradeMode.mDuration = TimeUtility.daysBetween(dateList.get(tradeMode.bpIdxList.get(tradeMode.bpIdxList.size() - 1)), TimeUtility.getCurrentDate());
            tradeMode.mBSPoint = closeCheckList.get(tradeMode.bpIdxList.get(tradeMode.bpIdxList.size() - 1));
            tradeMode.mMeanDate = (getMeanPositionDays(tradeMode.bpIdxList, tradeMode.spIdxList) * tradeMode.spIdxList.size() + tradeMode.mDuration) / tradeMode.bpIdxList.size();
            tradeMode.mPosRate = 100 * (getPositionDaysRate(tradeMode.bpIdxList, tradeMode.spIdxList) * dateList.size() + dateList.size() - tradeMode.bpIdxList.get(tradeMode.bpIdxList.size() - 1)) / dateList.size();
        } else {
            tradeMode.mStatus = false;
            tradeMode.mDuration = TimeUtility.daysBetween(dateList.get(tradeMode.spIdxList.get(tradeMode.spIdxList.size() - 1)), TimeUtility.getCurrentDate());
            tradeMode.mBSPoint = closeCheckList.get(tradeMode.spIdxList.get(tradeMode.spIdxList.size() - 1));
            tradeMode.mMeanDate = getMeanPositionDays(tradeMode.bpIdxList, tradeMode.spIdxList);
            tradeMode.mPosRate = 100 * getPositionDaysRate(tradeMode.bpIdxList, tradeMode.spIdxList);
        }

        tradeMode.mKeyPoint = livermore.getLMKey(tradeMode.mModeName);
        tradeMode.mKeyRatio = 100 * (tradeMode.mKeyPoint - closeList.get(rows - 1)) / closeList.get(rows - 1);
    }

    public void sysMACD2Chk(BeanTradeMode tradeMode) {
        String[] ps = tradeMode.mModePara.split(",");
        int bp0 = Integer.parseInt(ps[0]);
        int bp1 = Integer.parseInt(ps[1]);

        MACD macd = new MACD(closeList, 12, 26, 9);
        macd.init();
        strategy = new StrategyTrade(closeList);
        strategy.macd = macd;

        for (int i = 0; i < rows; i++) {
            strategy.barDifCrossTrade(i, bp0, bp1);
        }
        tradeMode.bpIdxList = strategy.bpIdxList;
        tradeMode.spIdxList = strategy.spIdxList;
        if (tradeMode.bpIdxList.size() > tradeMode.spIdxList.size()) {
            tradeMode.mStatus = true;
            tradeMode.mDuration = TimeUtility.daysBetween(dateList.get(tradeMode.bpIdxList.get(tradeMode.bpIdxList.size() - 1)), TimeUtility.getCurrentDate());
            tradeMode.mBSPoint = closeCheckList.get(tradeMode.bpIdxList.get(tradeMode.bpIdxList.size() - 1));
            tradeMode.mMeanDate = (getMeanPositionDays(tradeMode.bpIdxList, tradeMode.spIdxList) * tradeMode.spIdxList.size() + tradeMode.mDuration) / tradeMode.bpIdxList.size();
            tradeMode.mPosRate = 100 * (getPositionDaysRate(tradeMode.bpIdxList, tradeMode.spIdxList) * dateList.size() + dateList.size() - tradeMode.bpIdxList.get(tradeMode.bpIdxList.size() - 1)) / dateList.size();
        } else {
            tradeMode.mStatus = false;
            tradeMode.mDuration = TimeUtility.daysBetween(dateList.get(tradeMode.spIdxList.get(tradeMode.spIdxList.size() - 1)), TimeUtility.getCurrentDate());
            tradeMode.mBSPoint = closeCheckList.get(tradeMode.spIdxList.get(tradeMode.spIdxList.size() - 1));
            tradeMode.mMeanDate = getMeanPositionDays(tradeMode.bpIdxList, tradeMode.spIdxList);
            tradeMode.mPosRate = 100 * getPositionDaysRate(tradeMode.bpIdxList, tradeMode.spIdxList);
        }

        double barKey = macd.getBARKey(bp0);
        double difKey = macd.getDIFKey(bp1);
        tradeMode.mKeyPoint = (barKey > difKey) ? barKey : difKey;
        tradeMode.mKeyRatio = 100 * (tradeMode.mKeyPoint - closeList.get(rows - 1)) / closeList.get(rows - 1);
    }

    public void sysMACDMAChk(BeanTradeMode tradeMode) {
        String[] ps = tradeMode.mModePara.split(",");
        int bp = Integer.parseInt(ps[0]);
        int mas = Integer.parseInt(ps[1]);
        int mal = Integer.parseInt(ps[2]);

        MACD macd = new MACD(closeList, 12, 26, 9);
        macd.init();
        MAL ma = new MAL(closeList);
        strategy = new StrategyTrade(closeList);
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
        tradeMode.bpIdxList = strategy.bpIdxList;
        tradeMode.spIdxList = strategy.spIdxList;
        if (tradeMode.bpIdxList.size() > tradeMode.spIdxList.size()) {
            tradeMode.mStatus = true;
            tradeMode.mDuration = TimeUtility.daysBetween(dateList.get(tradeMode.bpIdxList.get(tradeMode.bpIdxList.size() - 1)), TimeUtility.getCurrentDate());
            tradeMode.mBSPoint = closeCheckList.get(tradeMode.bpIdxList.get(tradeMode.bpIdxList.size() - 1));
            tradeMode.mMeanDate = (getMeanPositionDays(tradeMode.bpIdxList, tradeMode.spIdxList) * tradeMode.spIdxList.size() + tradeMode.mDuration) / tradeMode.bpIdxList.size();
            tradeMode.mPosRate = 100 * (getPositionDaysRate(tradeMode.bpIdxList, tradeMode.spIdxList) * dateList.size() + dateList.size() - tradeMode.bpIdxList.get(tradeMode.bpIdxList.size() - 1)) / dateList.size();
        } else {
            tradeMode.mStatus = false;
            tradeMode.mDuration = TimeUtility.daysBetween(dateList.get(tradeMode.spIdxList.get(tradeMode.spIdxList.size() - 1)), TimeUtility.getCurrentDate());
            tradeMode.mBSPoint = closeCheckList.get(tradeMode.spIdxList.get(tradeMode.spIdxList.size() - 1));
            tradeMode.mMeanDate = getMeanPositionDays(tradeMode.bpIdxList, tradeMode.spIdxList);
            tradeMode.mPosRate = 100 * getPositionDaysRate(tradeMode.bpIdxList, tradeMode.spIdxList);
        }

        double maKey = ma.getMAKey(mas, mal);
        double macdKey = macd.getMACDKey(tradeMode.mModeName, bp);
        tradeMode.mKeyPoint = (maKey > macdKey) ? maKey : macdKey;
        tradeMode.mKeyRatio = 100 * (tradeMode.mKeyPoint - closeList.get(rows - 1)) / closeList.get(rows - 1);
    }

    public void sysMACDLMChk(BeanTradeMode tradeMode) {
        String[] ps = tradeMode.mModePara.split(",");
        int bp = Integer.parseInt(ps[0]);
        int t1 = Integer.parseInt(ps[1]);
        int t2 = Integer.parseInt(ps[2]);

        MACD macd = new MACD(closeList, 12, 26, 9);
        macd.init();
        Livermore livermore = new Livermore(true, t1, t2);
        strategy = new StrategyTrade(closeList);
        strategy.macd = macd;
        strategy.livermore = livermore;

        for (int i = 0; i < rows; i++) {
            livermore.arithmetic(closeList.get(i));
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
        tradeMode.bpIdxList = strategy.bpIdxList;
        tradeMode.spIdxList = strategy.spIdxList;
        if (tradeMode.bpIdxList.size() > tradeMode.spIdxList.size()) {
            tradeMode.mStatus = true;
            tradeMode.mDuration = TimeUtility.daysBetween(dateList.get(tradeMode.bpIdxList.get(tradeMode.bpIdxList.size() - 1)), TimeUtility.getCurrentDate());
            tradeMode.mBSPoint = closeCheckList.get(tradeMode.bpIdxList.get(tradeMode.bpIdxList.size() - 1));
            tradeMode.mMeanDate = (getMeanPositionDays(tradeMode.bpIdxList, tradeMode.spIdxList) * tradeMode.spIdxList.size() + tradeMode.mDuration) / tradeMode.bpIdxList.size();
            tradeMode.mPosRate = 100 * (getPositionDaysRate(tradeMode.bpIdxList, tradeMode.spIdxList) * dateList.size() + dateList.size() - tradeMode.bpIdxList.get(tradeMode.bpIdxList.size() - 1)) / dateList.size();
        } else {
            tradeMode.mStatus = false;
            tradeMode.mDuration = TimeUtility.daysBetween(dateList.get(tradeMode.spIdxList.get(tradeMode.spIdxList.size() - 1)), TimeUtility.getCurrentDate());
            tradeMode.mBSPoint = closeCheckList.get(tradeMode.spIdxList.get(tradeMode.spIdxList.size() - 1));
            tradeMode.mMeanDate = getMeanPositionDays(tradeMode.bpIdxList, tradeMode.spIdxList);
            tradeMode.mPosRate = 100 * getPositionDaysRate(tradeMode.bpIdxList, tradeMode.spIdxList);
        }

        double lmKey = livermore.getLMKey(tradeMode.mModeName);
        double macdKey = macd.getMACDKey(tradeMode.mModeName, bp);
        tradeMode.mKeyPoint = (lmKey > macdKey) ? lmKey : macdKey;
        tradeMode.mKeyRatio = 100 * (tradeMode.mKeyPoint - closeList.get(rows - 1)) / closeList.get(rows - 1);
    }

    private double getMeanPositionDays(ArrayList<Integer> bpIdxList, ArrayList<Integer> spIdxList) {
        int days = 0;
        for (int i = 0; i < spIdxList.size(); i++) {
            days += TimeUtility.daysBetween(dateList, bpIdxList.get(i), spIdxList.get(i));
        }
        return (double) days / spIdxList.size();
    }

    private double getPositionDaysRate(ArrayList<Integer> bpIdxList, ArrayList<Integer> spIdxList) {
        int days = 0;
        for (int i = 0; i < spIdxList.size(); i++) {
            days += spIdxList.get(i) - bpIdxList.get(i);
        }
        return (double) days / dateList.size();
    }
}
