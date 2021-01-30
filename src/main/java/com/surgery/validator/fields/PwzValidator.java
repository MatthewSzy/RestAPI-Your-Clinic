package com.surgery.validator.fields;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PwzValidator {

    private PwzValidator(){}

    public static boolean checkIfPwzIsInvalid(String pwz) {

        String pwzRegex = "[1-9]+[0-9]{6}";
        Pattern pattern = Pattern.compile(pwzRegex);
        Matcher matcher = pattern.matcher(pwz);

        return !matcher.matches();
    }
}
