package com.example.common.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringUtilsTest {

    @Test
    void getValueFromCookies() {
        // given
        String stringCookies = "Authorization=Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIwIiwiZXhwIjoxNTkwNzM2NjU0LCJpYXQiOjE1OTAxMzE4NTR9.EU7Oe3eydiOrzyWZ3ms-ZAq7dcSkUuSM45PpHQqil7U; cookie1=hyhy; ll=hyyyyy; empty=";

        // when
        String actual = StringUtils.getValueFromCookies(stringCookies, "Authorization");

        // then
        String expected = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIwIiwiZXhwIjoxNTkwNzM2NjU0LCJpYXQiOjE1OTAxMzE4NTR9.EU7Oe3eydiOrzyWZ3ms-ZAq7dcSkUuSM45PpHQqil7U";
        assertEquals(expected, actual);
    }
}