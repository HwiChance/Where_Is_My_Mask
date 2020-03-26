package com.hwichance.android.WhereIsMyMask.utils;

import java.util.Calendar;

public class DayOfWeekCalculator {
    private final static String[] week
            = {"일", "월", "화", "수", "목", "금", "토"};

    public static String getDayOfWeek() {
        Calendar cal  = Calendar.getInstance();
        int weekNumber = cal.get(Calendar.DAY_OF_WEEK);

        return week[weekNumber - 1];
    }

    public static String getPurchaseInfo(String DayOfWeek) {
        switch (DayOfWeek) {
            case "월":
                return "- - - 1년생\n- - - 6년생";
            case "화":
                return "- - - 2년생\n- - - 7년생";
            case "수":
                return "- - - 3년생\n- - - 8년생";
            case "목":
                return "- - - 4년생\n- - - 9년생";
            case "금":
                return "- - - 0년생\n- - - 5년생";
            default:
                return "주중에\n못 산 사람";
        }
    }
}
