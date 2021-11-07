package com.craftinginterpreters.lox;

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

    public void startNewLexeme() {
        start = current;
    }

    public boolean atEndOfSource() {
        return current >= sourceText.length();
    }

    public String substring() {
        return sourceText.substring(start, current);
    }

    public char peekNext() {
        if (current + 1 >= sourceText.length()) return '\0';
        return sourceText.charAt(current + 1);
    }

    public char advance() {
        return sourceText.charAt(current++);
    }

    public boolean match(char expected) {
        if (atEndOfSource()) return false;
        if (sourceText.charAt(current) != expected) return false;

        current++;
        return true;
    }

    public char peek() {
        if (atEndOfSource()) return '\0';
        return sourceText.charAt(current);
    }

    public void incrementLine() {
        line++;
    }

    public int getCurrentLine() {
        return line;
    }
}
