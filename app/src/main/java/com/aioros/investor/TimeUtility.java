package com.aioros.investor;

import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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

    public static boolean isCheckTime() {
        Calendar cal = Calendar.getInstance();
        if ((cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) || (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY))
            return false;
        return cal.get(Calendar.HOUR_OF_DAY) >= 15;
    }

    public static int daysBetween(ArrayList<String> dates, int idxs, int idxe) {
        String sdate = dates.get(idxs);
        String edate = dates.get(idxe);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Calendar cal = Calendar.getInstance();
        long between_days = 0;
        try {
            cal.setTime(dateFormat.parse(sdate));
            long time1 = cal.getTimeInMillis();
            cal.setTime(dateFormat.parse(edate));
            long time2 = cal.getTimeInMillis();
            between_days = (time2 - time1) / (1000 * 3600 * 24);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return (int) between_days;
    }

    public static int daysBetween(String sdate, String edate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Calendar cal = Calendar.getInstance();
        long between_days = 0;
        try {
            cal.setTime(dateFormat.parse(sdate));
            long time1 = cal.getTimeInMillis();
            cal.setTime(dateFormat.parse(edate));
            long time2 = cal.getTimeInMillis();
            between_days = (time2 - time1) / (1000 * 3600 * 24);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return Integer.parseInt(String.valueOf(between_days));
    }

    public static String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String currentDate = sdf.format(new Date());
        return currentDate;
    }
}
