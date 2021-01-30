package com.surgery.validator.fields;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordValidator {

    private PasswordValidator(){}

    public static boolean checkIfPasswordIsInvalid(String password) {

        String passwordRegex = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*_.-]).{8,}$";
        Pattern pattern = Pattern.compile(passwordRegex);
        Matcher matcher = pattern.matcher(password);

        return !matcher.matches();
    }
}
