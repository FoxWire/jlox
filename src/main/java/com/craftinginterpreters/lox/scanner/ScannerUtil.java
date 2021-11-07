package com.craftinginterpreters.lox.scanner;

public class ScannerUtil {

    public static boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }

    public static boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z')
                || (c >= 'A' && c <= 'Z')
                || c == '_';
    }

    public static boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    public static boolean isWhitespace(char c) {
        return c == ' ' || c == '\r' || c == '\t' || c == '\n';
    }

    public static boolean isNewLine(char c) {
        return c == '\n';
    }
}
