package com.aioros.investor;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by zhangxr on 2018/11/17.
 */

public class TradeHandle {
    public TradeHandle(ArrayList<String> dates, ArrayList<Double> closes, ArrayList<Integer> bpidxs, ArrayList<Integer> spidxs) {
        dateList = dates;
        closeList = closes;
        bpIdxList = bpidxs;
        spIdxList = spidxs;
        tradeDays = dateList.size();
    }

    protected void trade(boolean bs, double price) {
        if (bs) {
            bPrice = price;
            cash -= price;
            holdFlag = true;
        } else if (holdFlag) {
            sPrice = price;
            agioList.add(sPrice - bPrice);
            yieldList.add(100 * (sPrice - bPrice) / bPrice);
            cash += price;
            holdFlag = false;
        }
    }

    public ArrayList<Double> synthesize() {
        initAsset = closeList.get(bpIdxList.get(0));
        cash = initAsset;

        int times = bpIdxList.size();
        int startIdx = bpIdxList.get(0);
        int endIdx = spIdxList.get(times - 1);
        for (int i = 0; i < closeList.size(); i++) {
            if (i < startIdx) {
                fundList.add(initAsset);
            } else if (i <= endIdx) {
                if (bpIdxList.contains(i)) {
                    trade(true, closeList.get(i));
                } else if (spIdxList.contains(i)) {
                    trade(false, closeList.get(i));
                }
                fundList.add(getAsset(i));
            } else {
                fundList.add(fundList.get(endIdx));
            }
        }
        return fundList;
    }

    /**
     * * Start of report function
     */
    public double getAsset(int i) {
        if (holdFlag) {
            return cash + ratio * (closeList.get(i));
        } else {
            return cash;
        }
    }

    public double getCurrentAsset() {
        return getAsset(tradeDays - 1);
    }

    public double getInitAsset() {
        return initAsset;
    }

    public double getNetProfit() {
        double profit = 0;
        for (Double agio : agioList) {
            profit += agio;
        }
        return profit;
    }

    public double getObjectRate() {
        double sPrice = closeList.get(0);
        double ePrice = closeList.get(tradeDays - 1);
        return (double) 100 * (ePrice - sPrice) / sPrice;
    }

    public double getSystemRate() {
        double profit = getNetProfit();
        return (double) 100 * profit / initAsset;
    }

    public double getSysObjRatio() {
        return getSystemRate() / getObjectRate();
    }

    public double getAnnualRate() {
        double years = getTradeYears();
        double rate = (initAsset + getNetProfit()) / initAsset;
        return (double) 100 * (Math.pow(rate, (double) 1 / years) - 1);
    }

    public double getTradeYears() {
        return (double) TimeUtility.daysBetween(dateList, 0, tradeDays - 1) / 365.25;
    }

    public double getPositionYears() {
        int days = 0;
        for (int i = 0; i < bpIdxList.size(); i++) {
            days += TimeUtility.daysBetween(dateList, bpIdxList.get(i), spIdxList.get(i));
        }
        return (double) days / 365.25;
    }

    public double getPositionDaysRate() {
        int days = 0;
        for (int i = 0; i < bpIdxList.size(); i++) {
            days += spIdxList.get(i) - bpIdxList.get(i);
        }

        double rate = (double) 100 * days / tradeDays;
        return rate;
    }

    public double getMeanPositionDays() {
        int days = 0;
        for (int i = 0; i < bpIdxList.size(); i++) {
            days += TimeUtility.daysBetween(dateList, bpIdxList.get(i), spIdxList.get(i));
        }
        return (double) days / bpIdxList.size();
    }

    public double getMeanGainDays() {
        int days = 0;
        int times = 0;
        for (int i = 0; i < bpIdxList.size(); i++) {
            if (closeList.get(spIdxList.get(i)) > closeList.get(bpIdxList.get(i))) {
                days += TimeUtility.daysBetween(dateList, bpIdxList.get(i), spIdxList.get(i));
                times++;
            }
        }
        return (double) ((times > 0) ? days / times : 0);
    }

    public double getMeanLossDays() {
        int days = 0;
        int times = 0;
        for (int i = 0; i < bpIdxList.size(); i++) {
            if (closeList.get(spIdxList.get(i)) <= closeList.get(bpIdxList.get(i))) {
                days += TimeUtility.daysBetween(dateList, bpIdxList.get(i), spIdxList.get(i));
                times++;
            }
        }
        return (double) ((times > 0) ? days / times : 0);
    }

    public double getStandardAnnualRate() {
        double years = getTradeYears();
        double totalYield = 0;
        for (Double yield : yieldList) {
            totalYield += yield;
        }
        return totalYield / years;
    }

    public double getPositionAnnualRate() {
        double totalYield = 0;
        for (Double yield : yieldList) {
            totalYield += yield;
        }
        int totalDays = 0;
        for (int i = 0; i < bpIdxList.size(); i++) {
            totalDays += TimeUtility.daysBetween(dateList, bpIdxList.get(i), spIdxList.get(i));
        }

        return totalYield * 365.25 / totalDays;
    }

    public double getEvenEarningRate() {
        double totalYield = 0;
        for (Double yield : yieldList) {
            totalYield += yield;
        }
        return totalYield / yieldList.size();
    }

    public int getTradeTimes() {
        return agioList.size();
    }

    public int getGainTimes() {
        int times = 0;
        for (Double agio : agioList) {
            if (agio > 0) {
                times++;
            }
        }
        return times;
    }

    public int getLossTimes() {
        return getTradeTimes() - getGainTimes();
    }

    public double getWinRate() {
        int gainTimes = getGainTimes();
        return (double) 100 * gainTimes / agioList.size();
    }

    public double getMeanGain() {
        double totalYield = 0;
        int times = 0;
        for (Double yield : yieldList) {
            if (yield > 0) {
                totalYield += yield;
                times++;
            }
        }
        if (times > 0) {
            return (double) totalYield / times;
        }
        return 0;
    }

    public double getMeanLoss() {
        double totalYield = 0;
        int times = 0;
        for (Double yield : yieldList) {
            if (yield <= 0) {
                totalYield += yield;
                times++;
            }
        }
        if (times > 0) {
            return (double) totalYield / times;
        }
        return 0;
    }

    public double getOdds() {
        double gain = getMeanGain();
        double loss = getMeanLoss();
        return (double) gain / (-loss);
    }

    public double getExpectation() {
        double winRate = getWinRate() / 100;
        double odds = getOdds();
        return (double) (winRate * odds - (1 - winRate));
    }

    public double getGainProfit() {
        double profit = 0;
        for (Double agio : agioList) {
            if (agio > 0) {
                profit += agio;
            }
        }
        return profit;
    }

    public double getLossProfit() {
        double profit = 0;
        for (Double agio : agioList) {
            if (agio <= 0) {
                profit += agio;
            }
        }
        return profit;
    }

    public double getMaxGain() {
        if (yieldList.size() > 0) {
            return (double) ((Collections.max(yieldList) > 0) ? Collections.max(yieldList) : 0);
        }
        return 0;
    }

    public double getMaxLoss() {
        if (yieldList.size() > 0) {
            return (double) ((Collections.min(yieldList) > 0) ? 0 : Collections.min(yieldList));
        }
        return 0;
    }

    public int getMaxGainTimes() {
        ArrayList<Integer> timesList = new ArrayList<>();
        int times = 0;
        for (Double agio : agioList) {
            if (agio > 0) {
                times++;
            } else {
                if (times != 0) {
                    timesList.add(times);
                }
                times = 0;
            }
        }
        if (times != 0) {
            timesList.add(times);
        }
        return (timesList.size() > 0) ? Collections.max(timesList) : 0;
    }

    public int getMaxLossTimes() {
        ArrayList<Integer> timesList = new ArrayList<>();
        int times = 0;
        for (Double agio : agioList) {
            if (agio <= 0) {
                times++;
            } else {
                if (times != 0) {
                    timesList.add(times);
                }
                times = 0;
            }
        }
        if (times != 0) {
            timesList.add(times);
        }
        return (timesList.size() > 0) ? Collections.max(timesList) : 0;
    }

    public double getMaxGainRatio() {
        ArrayList<Double> ratioList = new ArrayList<>();
        double ratio = 0;
        for (Double yield : yieldList) {
            if (yield > 0) {
                ratio += yield;
            } else {
                if (ratio != 0) {
                    ratioList.add(ratio);
                }
                ratio = 0;
            }
        }
        if (ratio != 0) {
            ratioList.add(ratio);
        }
        return (ratioList.size() > 0) ? Collections.max(ratioList) : 0;
    }

    public double getMaxLossRatio() {
        ArrayList<Double> ratioList = new ArrayList<>();
        double ratio = 0;
        for (Double yield : yieldList) {
            if (yield <= 0) {
                ratio += yield;
            } else {
                if (ratio != 0) {
                    ratioList.add(ratio);
                }
                ratio = 0;
            }
        }
        if (ratio != 0) {
            ratioList.add(ratio);
        }
        return (ratioList.size() > 0) ? Collections.min(ratioList) : 0;
    }

    private ArrayList<String> dateList = new ArrayList<>();
    private ArrayList<Double> closeList = new ArrayList<>();
    private ArrayList<Integer> bpIdxList = new ArrayList<>();
    private ArrayList<Integer> spIdxList = new ArrayList<>();
    public ArrayList<Double> agioList = new ArrayList<>();
    public ArrayList<Double> yieldList = new ArrayList<>();
    public ArrayList<Double> fundList = new ArrayList<>();
    public double cash = 0;
    public double initAsset = 0;
    private double ratio = 1;
    public boolean holdFlag = false;
    public double bPrice = 0;
    public double sPrice = 0;
    public int tradeDays = 0;
}

