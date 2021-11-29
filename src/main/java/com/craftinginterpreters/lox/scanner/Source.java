package com.craftinginterpreters.lox.scanner;

import static com.craftinginterpreters.lox.scanner.ScannerUtil.*;

public class Source {

    private final String sourceText;

    private int start;
    private int current;
    private int line;

    public Source(String sourceText) {

        this.sourceText = sourceText;

        start = 0;
        line = 1;
        current = 0;
    }

    public void startLexeme() {
        start = current;
    }

    public boolean atEndOfSource() {
        return current >= sourceText.length();
    }

    public String readLexeme() {

        return sourceText.substring(start, current);
    }

    public char readChar() {
        char c = sourceText.charAt(current++);
        if (isNewLine(c)){
            line++;
        }
        return c;
    }

    public boolean matchChar(char expected) {
        if (atEndOfSource()) return false;
        if (sourceText.charAt(current) != expected) return false;

        current++;
        return true;
    }

    public char peek() {
        if (atEndOfSource()) return '\0';
        return sourceText.charAt(current);
    }

    public char peekNext() {
        if (current + 1 >= sourceText.length()) return '\0';
        return sourceText.charAt(current + 1);
    }

    public int getCurrentLine() {
        return line;
    }
}
