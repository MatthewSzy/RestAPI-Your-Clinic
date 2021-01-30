package com.surgery.validator.fields;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator {

    private EmailValidator() {}

    public static boolean checkIfEmailIsInvalid(String email) {

        String [] tokens = email.split("@");
        if (tokens.length != 2)
            return true;

        if (checkTokens(email, "\\."))
            return true;

        if (checkTokens(email, "_"))
            return true;

        if (checkTokens(email, "-"))
            return true;

        if (checkTokens(email, ","))
            return true;

        if (!Character.isAlphabetic(email.charAt(0)))
            return true;

        String emailRegex = "^[\\w.]+@[\\w.]+\\.[\\w]+$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);

        return !matcher.matches();
    }

    private static boolean checkTokens(String email, String regex) {
        String[] tokens = email.split(regex);
        for (String token : tokens) {
            if (token.length() == 0)
                return true;
        }

        return false;
    }
}
