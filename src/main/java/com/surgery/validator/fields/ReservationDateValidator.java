package com.surgery.validator.fields;

import java.sql.Date;
import java.util.Calendar;

public class ReservationDateValidator {

    private ReservationDateValidator(){}

    public static boolean checkIfStringHasIncorrectDate(String string) {

        String[] tokens = string.split("-");

        if (tokens.length != 3) {
            return true;
        }

        int year = Integer.parseInt(tokens[0]);
        int month = Integer.parseInt(tokens[1]);
        int day = Integer.parseInt(tokens[2]);

        if (year == Calendar.getInstance().get(Calendar.YEAR) &&
            month == Calendar.getInstance().get(Calendar.MONTH) + 1 &&
            day <= Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) {

            return true;
        }

        if (year == Calendar.getInstance().get(Calendar.YEAR) &&
            month < Calendar.getInstance().get(Calendar.MONTH) + 1) {

            return true;
        }

        if (year < Calendar.getInstance().get(Calendar.YEAR)) {
            return true;
        }

        if (day < 1 || day > 31) {
            return true;
        }

        if (month < 1 || month > 12) {
            return true;
        }

        boolean yearType = checkIfYearIsLeap(year);

        if (month == 2) {
            if (yearType && day != 29) {
                return true;
            }
            else if (!yearType && day != 28) {
                return true;
            }
        }

        return returnCountOfDaysInMonth(month, yearType) < day;
    }

    public static Date getDateFromString(String string) {

        String[] tokens = string.split("-");

        int year = Integer.parseInt(tokens[0]);
        int month = Integer.parseInt(tokens[1]);
        int day = Integer.parseInt(tokens[2]);

        String stringDate = String.format("%4d-%02d-%02d", year, month, day);

        return Date.valueOf(stringDate);
    }

    private static boolean checkIfYearIsLeap(int year) {
        return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
    }

    private static int returnCountOfDaysInMonth(int month, boolean yearType) {
        if (month == 4 ||
            month == 6 ||
            month == 9 ||
            month == 11) {

            return 30;
        }
        else if (month == 2) {
            if (yearType)
                return 29;
            else
                return 28;
        }

        return 31;
    }
}
