package com.aioros.investor;

/**
 * Created by aizhang on 2017/6/25.
 */

public class Livermore {

    public Livermore(boolean status, int t1, int t2) {
        STATUST = status ? 1 : -1;
        TP1 = t1;
        TP2 = t2;
    }

    public String mainRiseProc(double price) {
        String msg = "";
        if (price > mainRiseVal) {
            mainRiseVal = price;
        } else if (price < mainFallVal) {
            STATUST = -1;
            resetValue(1);
            mainFallVal = price;
            msg = "↘↘↘↘ 恢复下降趋势";
        } else if (price < riseKeyVal * (100 - TP2) / 100) {
            STATUST = -1;
            resetValue(0);
            mainFallVal = price;
            msg = "↘↘↘↘↘↘ 进入下降趋势";
        } else if (price < (mainRiseVal * (100 - TP1) / 100)) {
            STATUST = 2;
            normalFallUVal = price;
            msg = "↗ 进入自然回撤";
        }
        return msg;
    }

    public String normalFallUProc(double price) {
        String msg = "";
        if (price < mainFallVal) {
            STATUST = -1;
            resetValue(1);
            mainFallVal = price;
            msg = "↘↘↘↘ 恢复下降趋势";
        } else if (price < riseKeyVal * (100 - TP2) / 100) {
            STATUST = -1;
            resetValue(0);
            mainFallVal = price;
            msg = "↘↘↘↘↘↘ 进入下降趋势";
        } else if (price < normalFallUVal) {
            normalFallUVal = price;
        } else if (price > mainRiseVal) {
            STATUST = 1;
            if (normalFallUVal > riseKeyVal) {
                riseKeyVal = normalFallUVal;
            }
            resetValue(1);
            mainRiseVal = price;
            msg = "↗↗↗ 恢复上升趋势";
        } else if (price > (normalFallUVal * (100 + TP1) / 100)) {
            STATUST = 3;
            normalRiseUVal = price;
            if (normalFallUVal > riseKeyVal) {
                riseKeyVal = normalFallUVal;
            }
            msg = "↗ 进入自然回升";
        }
        return msg;
    }

    public String normalRiseUProc(double price) {
        String msg = "";
        if (price > mainRiseVal) {
            STATUST = 1;
            resetValue(1);
            mainRiseVal = price;
            msg = "↗↗↗ 恢复上升趋势";
        } else if (price > normalRiseUVal) {
            normalRiseUVal = price;
        } else if (price < mainFallVal) {
            STATUST = -1;
            resetValue(1);
            mainFallVal = price;
            msg = "↘↘↘↘ 恢复下降趋势";
        } else if (price < riseKeyVal * (100 - TP2) / 100) {
            STATUST = -1;
            resetValue(0);
            mainFallVal = price;
            msg = "↘↘↘↘↘↘ 进入下降趋势";
        } else if (price < normalFallUVal) {
            STATUST = 2;
            normalFallUVal = price;
            msg = "↗ 进入自然回撤";
        } else if (price < (normalRiseUVal * (100 - TP1) / 100)) {
            STATUST = 4;
            minorFallUVal = price;
            msg = "↗ 进入次级回撤";
        } else if (price < (normalRiseUVal * (100 - TP2) / 100)) {
            msg = "↗ 上升趋势可能改变";
        }
        return msg;
    }

    public String minorFallUProc(double price) {
        String msg = "";
        if (price < mainFallVal) {
            STATUST = -1;
            resetValue(1);
            mainFallVal = price;
            msg = "↘↘↘↘ 恢复下降趋势";
        } else if (price < riseKeyVal * (100 - TP2) / 100) {
            STATUST = -1;
            resetValue(0);
            mainFallVal = price;
            msg = "↘↘↘↘↘↘ 进入下降趋势";
        } else if (price < normalFallUVal) {
            STATUST = 2;
            normalFallUVal = price;
            msg = "↗ 恢复自然回撤";
        } else if (price < minorFallUVal) {
            minorFallUVal = price;
        } else if (price > mainRiseVal) {
            STATUST = 1;
            resetValue(1);
            mainRiseVal = price;
            msg = "↗↗↗ 恢复上升趋势";
        } else if (price > normalRiseUVal) {
            STATUST = 3;
            normalRiseUVal = price;
            msg = "↗ 恢复自然回升";
        } else if (price > minorFallUVal * (100 + TP1) / 100) {
            STATUST = 5;
            minorRiseUVal = price;
            msg = "↗ 进入次级回升";
        }
        return msg;
    }

    public String minorRiseUProc(double price) {
        String msg = "";
        if (price > mainRiseVal) {
            STATUST = 1;
            resetValue(1);
            mainRiseVal = price;
            msg = "↗↗↗ 恢复上升趋势";
        } else if (price > normalRiseUVal) {
            STATUST = 3;
            normalRiseUVal = price;
            msg = "↗ 恢复自然回升";
        } else if (price > minorRiseUVal) {
            minorRiseUVal = price;
        } else if (price < mainFallVal) {
            STATUST = -1;
            resetValue(1);
            mainFallVal = price;
            msg = "↘↘↘↘ 恢复下降趋势";
        } else if (price < riseKeyVal * (100 - TP2) / 100) {
            STATUST = -1;
            resetValue(0);
            mainFallVal = price;
            msg = "↘↘↘↘↘↘ 进入下降趋势";
        } else if (price < normalFallUVal) {
            STATUST = 2;
            normalFallUVal = price;
            msg = "↗ 恢复自然回撤";
        } else if (price < minorRiseUVal * (100 - TP1) / 100) {
            STATUST = 4;
            minorFallUVal = price;
            msg = "↗ 进入次级回撤";
        }
        return msg;
    }

    public String mainFallProc(double price) {
        String msg = "";
        if ((price < mainFallVal) || (mainFallVal == 0)) {
            mainFallVal = price;
        } else if (price > mainRiseVal) {
            STATUST = 1;
            resetValue(1);
            mainRiseVal = price;
            msg = "↗↗↗↗ 恢复上升趋势";
        } else if ((fallKeyVal > 0) && (price > fallKeyVal * (100 + TP2) / 100)) {
            STATUST = 1;
            resetValue(0);
            mainRiseVal = price;
            msg = "↗↗↗↗↗↗ 进入上升趋势";
        } else if (price > (mainFallVal * (100 + TP1) / 100)) {
            STATUST = -2;
            normalRiseDVal = price;
            msg = "↘ 进入自然回升";
        }
        return msg;
    }

    public String normalRiseDProc(double price) {
        String msg = "";
        if ((mainRiseVal > 0) && (price > mainRiseVal)) {
            STATUST = 1;
            resetValue(1);
            mainRiseVal = price;
            msg = "↗↗↗↗ 恢复上升趋势";
        } else if ((fallKeyVal > 0) && (price > fallKeyVal * (100 + TP2) / 100)) {
            STATUST = 1;
            resetValue(0);
            mainRiseVal = price;
            msg = "↗↗↗↗↗↗ 进入上升趋势";
        } else if (price > normalRiseDVal) {
            normalRiseDVal = price;
        } else if (price < mainFallVal) {
            STATUST = -1;
            if ((normalRiseDVal < fallKeyVal) || (fallKeyVal == 0)) {
                fallKeyVal = normalRiseDVal;
            }
            resetValue(1);
            mainFallVal = price;
            msg = "↘↘↘ 恢复下降趋势";
        } else if (price < (normalRiseDVal * (100 - TP1) / 100)) {
            STATUST = -3;
            normalFallDVal = price;
            if ((normalRiseDVal < fallKeyVal) || (fallKeyVal == 0)) {
                fallKeyVal = normalRiseDVal;
            }
            msg = "↘ 进入自然回撤";
        }
        return msg;
    }

    public String normalFallDProc(double price) {
        String msg = "";
        if (price < mainFallVal) {
            STATUST = -1;
            resetValue(1);
            mainFallVal = price;
            msg = "↘↘↘ 恢复下降趋势";
        } else if (price < normalFallDVal) {
            normalFallDVal = price;
        } else if (price > mainRiseVal) {
            STATUST = 1;
            resetValue(1);
            mainRiseVal = price;
            msg = "↗↗↗↗ 恢复上升趋势";
        } else if (price > fallKeyVal * (100 + TP2) / 100) {
            STATUST = 1;
            resetValue(0);
            mainRiseVal = price;
            msg = "↗↗↗↗↗↗ 进入上升趋势";
        } else if (price > normalRiseDVal) {
            STATUST = -2;
            normalRiseDVal = price;
            msg = "↘ 进入自然回升";
        } else if (price > (normalFallDVal * (100 + TP1) / 100)) {
            STATUST = -4;
            minorRiseDVal = price;
            msg = "↘ 进入次级回升";
        } else if (price > (normalFallDVal * (100 + TP2) / 100)) {
            msg = "↘ 下降趋势可能改变";
        }
        return msg;
    }

    public String minorRiseDProc(double price) {
        String msg = "";
        if ((mainRiseVal > 0) && (price > mainRiseVal)) {
            STATUST = 1;
            resetValue(1);
            mainRiseVal = price;
            msg = "↗↗↗↗ 恢复上升趋势";
        } else if (price > fallKeyVal * (100 + TP2) / 100) {
            STATUST = 1;
            resetValue(0);
            mainRiseVal = price;
            msg = "↗↗↗↗↗↗ 进入上升趋势";
        } else if (price > normalRiseDVal) {
            STATUST = -2;
            normalRiseDVal = price;
            msg = "↘ 恢复自然回升";
        } else if (price > minorRiseDVal) {
            minorRiseDVal = price;
        } else if (price < mainFallVal) {
            STATUST = -1;
            resetValue(1);
            mainFallVal = price;
            msg = "↘↘↘ 恢复下降趋势";
        } else if (price < normalFallDVal) {
            STATUST = -3;
            normalFallDVal = price;
            msg = "↘ 恢复自然回撤";
        } else if (price < minorRiseDVal * (100 - TP1) / 100) {
            STATUST = -5;
            minorFallDVal = price;
            msg = "↘ 进入次级回撤";
        }
        return msg;
    }

    public String minorFallDProc(double price) {
        String msg = "";
        if (price < mainFallVal) {
            STATUST = -1;
            resetValue(1);
            mainFallVal = price;
            msg = "↘↘↘ 恢复下降趋势";
        } else if (price < normalFallDVal) {
            STATUST = -3;
            normalFallDVal = price;
            msg = "↘ 恢复自然回撤";
        } else if (price < minorFallDVal) {
            minorFallDVal = price;
        } else if (price > mainRiseVal) {
            STATUST = 1;
            resetValue(1);
            mainRiseVal = price;
            msg = "↗↗↗↗ 恢复上升趋势";
        } else if (price > fallKeyVal * (100 + TP2) / 100) {
            STATUST = 1;
            resetValue(0);
            mainRiseVal = price;
            msg = "↗↗↗↗↗↗ 进入上升趋势";
        } else if (price > normalRiseDVal) {
            STATUST = -2;
            normalRiseDVal = price;
            msg = "↘ 恢复自然回升";
        } else if (price > minorFallDVal * (100 + TP1) / 100) {
            STATUST = -4;
            minorRiseDVal = price;
            msg = "↘ 进入次级回升";
        }
        return msg;
    }

    protected String arithmetic(double price) {
        STATUSY = STATUST;
        switch (STATUSY) {
            case 1: // 主上升
                return mainRiseProc(price);
            case 2: // 自然回撤
                return normalFallUProc(price);
            case 3: // 自然回升
                return normalRiseUProc(price);
            case 4: // 次级回撤
                return minorFallUProc(price);
            case 5: // 次级回升
                return minorRiseUProc(price);

            case -1: // 主下降
                return mainFallProc(price);
            case -2: // 自然回升
                return normalRiseDProc(price);
            case -3: // 自然回撤
                return normalFallDProc(price);
            case -4: // 次级回升
                return minorRiseDProc(price);
            case -5: // 次级回撤
                return minorFallDProc(price);
            default:
                return "";
        }
    }

    protected void resetValue(int level) {
        switch (STATUST) {
            case 1:
                if (level == 0) {
                    riseKeyVal = 0;
                }
                mainRiseVal = 0;
                normalRiseUVal = 0;
                normalFallUVal = 0;
                minorRiseUVal = 0;
                minorFallUVal = 0;
                break;
            case -1:
                if (level == 0) {
                    fallKeyVal = 0;
                }
                mainFallVal = 0;
                normalRiseDVal = 0;
                normalFallDVal = 0;
                minorRiseDVal = 0;
                minorFallDVal = 0;
                break;
            default:
                break;
        }
    }

    public double getLMKey(String mode) {
        if (mode.contains("LML")) {
            return getLMLKey();
        } else {
            return getLMSKey();
        }
    }

    public double getLMLKey() {
        if (STATUST > 0) {
            double key = riseKeyVal * (100 - TP2) / 100;
            return (key > mainFallVal) ? key : mainFallVal;
        } else {
            if (fallKeyVal == 0) {
                return mainRiseVal;
            }
            double key = fallKeyVal * (100 + TP2) / 100;
            return (key < mainRiseVal) ? key : mainRiseVal;
        }
    }

    public double getLMSKey() {
        if (STATUST == 1) {
            return mainRiseVal * (100 - TP1) / 100;
        } else if (STATUST > 1) {
            return mainRiseVal;
        } else {
            if (fallKeyVal == 0) {
                return mainRiseVal;
            }
            double key = fallKeyVal * (100 + TP2) / 100;
            return (key < mainRiseVal) ? key : mainRiseVal;
        }
    }

    public boolean enterRiseTrend() {
        return (STATUSY < 0) && (STATUST > 0);
    }

    public boolean enterFallTrend() {
        return (STATUSY > 0) && (STATUST < 0);
    }

    public boolean enterMainRise() {
        return (STATUSY != 1) && (STATUST == 1);
    }

    public boolean exitMainRise() {
        return (STATUSY == 1) && (STATUST != 1);
    }

    public double riseKeyVal = 0;
    public double fallKeyVal = 0;
    public double mainRiseVal = 0;
    public double mainFallVal = 0;
    public double normalRiseUVal = 0;
    public double normalFallUVal = 0;
    public double normalRiseDVal = 0;
    public double normalFallDVal = 0;
    public double minorRiseUVal = 0;
    public double minorFallUVal = 0;
    public double minorRiseDVal = 0;
    public double minorFallDVal = 0;

    public int STATUST = 1;
    public int STATUSY = -1;
    public int TP1 = 10;
    public int TP2 = 5;

}

