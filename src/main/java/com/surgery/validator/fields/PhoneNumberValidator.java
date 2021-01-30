package com.surgery.validator.fields;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneNumberValidator {

    private PhoneNumberValidator() {}

    public static boolean checkIfPhoneNumberIsInvalid(String phoneNumber) {

        String phoneNumberRegex = "[0-9]{9}";
        Pattern pattern = Pattern.compile(phoneNumberRegex);
        Matcher matcher = pattern.matcher(phoneNumber);

        return !matcher.matches();
    }
}
