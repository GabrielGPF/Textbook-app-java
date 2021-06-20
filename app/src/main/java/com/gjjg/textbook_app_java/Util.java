package com.gjjg.textbook_app_java;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static String guessName(String input){
        String result = input.split("[-_.@? ]")[0];
        result = result.substring(0, 1).toUpperCase() + result.substring(1);
        return result;
    }
}
