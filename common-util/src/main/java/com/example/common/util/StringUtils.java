package com.example.common.util;

import javafx.util.Pair;

import java.util.Arrays;

public class StringUtils {
    public static String getValueFromCookies(String stringCookies, String key) {
        if (stringCookies == null || key == null) {
            return null;
        }

        return Arrays
                .stream(stringCookies.split(";")).map(element -> {
                    String[] keyValue = element.split("=");
                    return new Pair<>(keyValue[0], keyValue[1]);
                })
                .filter(pair -> pair.getKey().equals(key))
                .findAny()
                .map(Pair::getValue)
                .orElse(null);
    }
}
