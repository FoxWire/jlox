package com.craftinginterpreters.lox.scanner;

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

    /**
     * .
     * @return a char and advances the counter
     */
    public char read() {
        char c = sourceText.charAt(current);
        current++;
        return c;
    }

    /**
     * Checks if the next char in the source text matches the given char. If so advances
     * the pointer and returns true, else just returns false.
     * @param next
     * @return
     */
    public boolean readIf(char next) {
        char currentChar = sourceText.charAt(current);
        if (currentChar == next) {
            current++;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Takes a char sequence of some kind, runs up to the sequence and returns the sequence.
     * This can be used for reading comments and strings, that have delimiters.
     * @return
     */
    public String readTo(String s) {
        int targetIndex = sourceText.indexOf(s);
        String result = sourceText.substring(current, targetIndex);
        current = targetIndex;
        return result;
    }

    public String readTo(char c){
        return readTo(c + "");
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
