package com.surgery.validator.fields;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PeselValidator {

    private PeselValidator() {}

    public static boolean checkIfPeselNumberIsInvalid(String pesel) {

        String peselRegex = "[0-9]{11}";
        Pattern pattern = Pattern.compile(peselRegex);
        Matcher matcher = pattern.matcher(pesel);

        return !matcher.matches();
    }
}
