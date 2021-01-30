package com.surgery.validator.fields;

public class GenderValidator {

    private GenderValidator(){}

    public static String getGender(String pesel) {

        int number = pesel.charAt(pesel.length() - 2);

        return (number % 2) == 0 ? "Kobieta" : "Mężczyzna";
    }
}
