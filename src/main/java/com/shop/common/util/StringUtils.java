package com.shop.common.util;

public class StringUtils {

    public static String obfuscatedEmail(String email) {
        String[] emailParts = email.split("@");
        return emailParts[0].charAt(0) + emailParts[0].substring(1).replaceAll(".", "*") + "@" + emailParts[1];
    }
}
