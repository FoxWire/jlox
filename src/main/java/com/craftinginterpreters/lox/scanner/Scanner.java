package com.craftinginterpreters.lox.scanner;

import com.craftinginterpreters.lox.Lox;
import com.craftinginterpreters.lox.Token;
import com.craftinginterpreters.lox.TokenType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.craftinginterpreters.lox.scanner.ScannerUtil.*;
import static com.craftinginterpreters.lox.TokenType.*;

public class Scanner {

    private final List<Token> tokens;
    private final Source source;

    private static final Map<String, TokenType> KEYWORDS = initKeywords();

    private static HashMap<String, TokenType> initKeywords() {
        HashMap<String, TokenType> keywords = new HashMap<>();
        keywords.put("and", AND);
        keywords.put("class", CLASS);
        keywords.put("else", ELSE);
        keywords.put("false", FALSE);
        keywords.put("for", FOR);
        keywords.put("fun", FUN);
        keywords.put("if", IF);
        keywords.put("nil", NIL);
        keywords.put("or", OR);
        keywords.put("print", PRINT);
        keywords.put("return", RETURN);
        keywords.put("super", SUPER);
        keywords.put("this", THIS);
        keywords.put("true", TRUE);
        keywords.put("var", VAR);
        keywords.put("while", WHILE);
        return keywords;
    }

    public Scanner(String sourceText) {
        source = new Source(sourceText);
        tokens = new ArrayList<>();
    }

    public List<Token> scanTokens() {
        while (!source.atEndOfSource()) {
            source.startNewLexeme();
            scanToken();
        }

        tokens.add(new Token(EOF, "", null, source.getCurrentLine()));
        return tokens;
    }

    private void scanToken() {
        char c = source.advance();
        switch (c) {
            case '(' -> addToken(LEFT_PAREN);
            case ')' -> addToken(RIGHT_PAREN);
            case '{' -> addToken(LEFT_BRACE);
            case '}' -> addToken(RIGHT_BRACE);
            case ',' -> addToken(COMMA);
            case '.' -> addToken(DOT);
            case '-' -> addToken(MINUS);
            case '+' -> addToken(PLUS);
            case ';' -> addToken(SEMICOLON);
            case '*' -> addToken(STAR);
            case '!' -> addToken(source.match('=') ? BANG_EQUAL : BANG);
            case '=' -> addToken(source.match('=') ? EQUAL_EQUAL : EQUAL);
            case '<' -> addToken(source.match('=') ? LESS_EQUAL : LESS);
            case '>' -> addToken(source.match('=') ? GREATER_EQUAL : GREATER);
            case ' ', '\r', '\t' -> {
            } // Ignore whitespace
            case '\n' -> source.incrementLine();
            case '/' -> handleComment();
            case '"' -> string();
            default -> {
                if (isDigit(c)) {
                    number();
                } else if (isAlpha(c)) {
                    identifier();
                } else {
                    Lox.error(source.getCurrentLine(), "Unexpected character");
                }
            }
        }
    }

    private void identifier() {
        while (isAlphaNumeric(source.peek()))
            source.advance();

        String text = source.substring();
        TokenType tokenType = KEYWORDS.getOrDefault(text, IDENTIFIER);
        addToken(tokenType);
    }

    private void number() {
        while (isDigit(source.peek()))
            source.advance();

        if (source.peek() == '.' && isDigit(source.peekNext())) {
            source.advance();

            while (isDigit(source.peek()))
                source.advance();
        }

        addToken(NUMBER, Double.parseDouble(source.substring()));
    }

    private void handleComment() {
        if (!source.match('/')) {
            addToken(SLASH);
            return;
        }

        while (source.peek() != '\n' && !source.atEndOfSource()) {
            source.advance(); // Advance until the end of the line, ignoring commented-out code.
        }
    }

    private void string() {

        while (source.peek() != '"' && !source.atEndOfSource()) {

            if (source.peek() == '\n') source.incrementLine();
            source.advance();
        }

        if (source.atEndOfSource()) {
            Lox.error(source.getCurrentLine(), "Unterminated string.");
            return;
        }

        // The closing ".
        source.advance();

        // Trim the surrounding quotes
        String lexeme = source.substring();
        String value = lexeme.substring(1, lexeme.length() - 1);
        addToken(STRING, value);
    }

    private void addToken(TokenType type) {
        addToken(type, null);
    }

    private void addToken(TokenType type, Object literal) {
        String text = source.substring();
        tokens.add(new Token(type, text, literal, source.getCurrentLine()));
    }
}
