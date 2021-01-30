package com.surgery.validator.fields;

import java.sql.Date;

public class DateOfBirthValidator {

    private DateOfBirthValidator(){}

    public static boolean checkIfPeselHasIncorrectDate(String pesel) {

        int yearOfBirth = Integer.parseInt(pesel.substring(0, 2));
        int monthOfBirth = Integer.parseInt(pesel.substring(2, 4));
        int dayOfBirth = Integer.parseInt(pesel.substring(4, 6));

        if (dayOfBirth <= 0 || dayOfBirth > 31) {
            return true;
        }

        if (monthOfBirth <= 0 || (monthOfBirth > 12 && monthOfBirth < 21) || monthOfBirth > 32) {
            return true;
        }

        boolean yearType = checkIfYearIsLeap(yearOfBirth);

        if (monthOfBirth == 2 || monthOfBirth == 22) {
            if (yearType && dayOfBirth > 29) {
                return true;
            }
            else if (!yearType && dayOfBirth > 28) {
                return true;
            }
        }

        if (monthOfBirth > 12) {
            monthOfBirth -= 20;
        }

        return returnCountOfDaysInMonth(monthOfBirth, yearType) < dayOfBirth;
    }

    public static Date getDateFromPesel(String pesel) {

        int yearOfBirth = Integer.parseInt(pesel.substring(0, 2));
        int monthOfBirth = Integer.parseInt(pesel.substring(2, 4));
        int dayOfBirth = Integer.parseInt(pesel.substring(4, 6));

        yearOfBirth = (monthOfBirth <= 12) ? (1900 + yearOfBirth) : (2000 + yearOfBirth);

        String stringDate = String.format("%4d-%02d-%02d", yearOfBirth, monthOfBirth, dayOfBirth);

        return Date.valueOf(stringDate);
    }

    private static boolean checkIfYearIsLeap(int year) {
        return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
    }

    private static int returnCountOfDaysInMonth(int month, boolean yearType) {
        if (month == 1 ||
            month == 3 ||
            month == 5 ||
            month == 7 ||
            month == 8 ||
            month == 10 ||
            month == 12) {

            return 31;
        }
        else if (month == 2) {
            if (yearType)
                return 29;
            else
                return 28;
        }

        return 30;
    }
}
