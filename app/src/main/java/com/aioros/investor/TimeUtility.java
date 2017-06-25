package com.aioros.investor;

import java.util.Calendar;

/**
 * Created by aizhang on 2017/6/25.
 */

public class TimeUtility {

    public static boolean isTradeTime() {
        Calendar cal = Calendar.getInstance();
        if ((cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) || (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY))
            return false;
        if ((cal.get(Calendar.HOUR_OF_DAY) < 9) || (cal.get(Calendar.HOUR_OF_DAY) >= 15))
            return false;
        if ((cal.get(Calendar.HOUR_OF_DAY) < 10) && (cal.get(Calendar.MINUTE) < 30))
            return false;
        if ((cal.get(Calendar.HOUR_OF_DAY) >= 11) && (cal.get(Calendar.MINUTE) > 30) && (cal.get(Calendar.HOUR_OF_DAY) < 13))
            return false;
        return true;
    }
}
