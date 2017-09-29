package com.aioros.investor;

/**
 * Created by aizhang on 2017/6/25.
 */

import java.util.ArrayList;

import static com.aioros.investor.FormulaLib.CROSS;
import static com.aioros.investor.FormulaLib.REFD;

public class Strategy {

    public Strategy(ArrayList<Double> pList) {
        priceList = pList;
    }

    public void barCrossTrade(int idx, double bp) {
        boolean b = CROSS(idx, macd.barList, bp);
        boolean s = CROSS(idx, bp, macd.barList);
        saveTradeIndex(idx, b, s);
    }

    public void difCrossTrade(int idx, double bp) {
        boolean b = CROSS(idx, macd.difList, bp);
        boolean s = CROSS(idx, bp, macd.difList);
        saveTradeIndex(idx, b, s);
    }

    public void barDifCrossTrade(int idx, double bp0, double bp1) {
        boolean c1 = (REFD(macd.barList, idx, 1) > bp0) && (REFD(macd.difList, idx, 1) > bp1);
        boolean c2 = (macd.barList.get(idx) > bp0) && (macd.difList.get(idx) > bp1);
        boolean b = (!c1) && c2;
        boolean s = c1 && (!c2);
        saveTradeIndex(idx, b, s);
    }

    public void maCrossTrade(int idx, ArrayList<Double> list) {
        boolean b = CROSS(idx, priceList, list);
        boolean s = CROSS(idx, list, priceList);
        saveTradeIndex(idx, b, s);
    }

    public void maCrossTrade(int idx, ArrayList<Double> sList, ArrayList<Double> lList) {
        boolean b = CROSS(idx, sList, lList);
        boolean s = CROSS(idx, lList, sList);
        saveTradeIndex(idx, b, s);
    }

    public void lmLongTrade(int idx) {
        boolean b = livermore.enterRiseTrend();
        boolean s = livermore.enterFallTrend();
        saveTradeIndex(idx, b, s);
    }

    public void lmShortTrade(int idx) {
        boolean b = livermore.enterMainRise();
        boolean s = livermore.exitMainRise();
        saveTradeIndex(idx, b, s);
    }

    public void barMACrossTrade(int idx, double value, ArrayList<Double> sList, ArrayList<Double> lList) {
        boolean c1 = (REFD(macd.barList, idx, 1) > value) && (REFD(sList, idx, 1) > REFD(lList, idx, 1));
        boolean c2 = (macd.barList.get(idx) > value) && (sList.get(idx) > lList.get(idx));
        boolean b = (!c1) && c2;
        boolean s = c1 && (!c2);
        saveTradeIndex(idx, b, s);
    }

    public void difMACrossTrade(int idx, double value, ArrayList<Double> sList, ArrayList<Double> lList) {
        boolean c1 = (REFD(macd.difList, idx, 1) > value) && (REFD(sList, idx, 1) > REFD(lList, idx, 1));
        boolean c2 = (macd.difList.get(idx) > value) && (sList.get(idx) > lList.get(idx));
        boolean b = (!c1) && c2;
        boolean s = c1 && (!c2);
        saveTradeIndex(idx, b, s);
    }

    public void barLMLCrossTrade(int idx, double value) {
        boolean c1 = (REFD(macd.barList, idx, 1) > value) && (livermore.STATUSY > 0);
        boolean c2 = (macd.barList.get(idx) > value) && (livermore.STATUST > 0);
        boolean b = (!c1) && c2;
        boolean s = c1 && (!c2);
        saveTradeIndex(idx, b, s);
    }

    public void difLMLCrossTrade(int idx, double value) {
        boolean c1 = (REFD(macd.difList, idx, 1) > value) && (livermore.STATUSY > 0);
        boolean c2 = (macd.difList.get(idx) > value) && (livermore.STATUST > 0);
        boolean b = (!c1) && c2;
        boolean s = c1 && (!c2);
        saveTradeIndex(idx, b, s);
    }

    public void barLMSCrossTrade(int idx, double value) {
        boolean c1 = (REFD(macd.barList, idx, 1) > value) && (livermore.STATUSY == 1);
        boolean c2 = (macd.barList.get(idx) > value) && (livermore.STATUST == 1);
        boolean b = (!c1) && c2;
        boolean s = c1 && (!c2);
        saveTradeIndex(idx, b, s);
    }

    public void difLMSCrossTrade(int idx, double value) {
        boolean c1 = (REFD(macd.difList, idx, 1) > value) && (livermore.STATUSY == 1);
        boolean c2 = (macd.difList.get(idx) > value) && (livermore.STATUST == 1);
        boolean b = (!c1) && c2;
        boolean s = c1 && (!c2);
        saveTradeIndex(idx, b, s);
    }

    public void saveTradeIndex(int idx, boolean buy, boolean sell) {
        if (buy) {
            bpIdxList.add(idx);
        }
        if (sell & (bpIdxList.size() > 0)) {
            spIdxList.add(idx);
        }
    }

    private ArrayList<Double> priceList = new ArrayList<>();
    public ArrayList<Integer> bpIdxList = new ArrayList<>();
    public ArrayList<Integer> spIdxList = new ArrayList<>();
    public MACD macd;
    public MAL ma;
    public Livermore livermore;
}

