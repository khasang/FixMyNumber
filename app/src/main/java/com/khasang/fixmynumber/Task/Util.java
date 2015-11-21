package com.khasang.fixmynumber.Task;

/**
 * Created by ivansv on 18.11.2015.
 */
public class Util {
    public static String onlyDigits(String testNumber) {
        for (int i = 0; i < testNumber.length(); i++) {
            if ((testNumber.charAt(i) == ' ') || (testNumber.charAt(i) == '-')) {
                testNumber = testNumber.substring(0, i) + testNumber.substring(i + 1);
            }
        }
        return testNumber;
    }
}
