package com.craftinginterpreters.lox.scanner;

import com.craftinginterpreters.lox.Lox;
import com.craftinginterpreters.lox.Token;
import com.craftinginterpreters.lox.TokenType;

import java.util.*;

import static com.craftinginterpreters.lox.scanner.ScannerUtil.*;
import static com.craftinginterpreters.lox.TokenType.*;
import static java.util.Objects.*;

public class Scanner {

    private static Source source;

    private Scanner() {}

    public static List<Token> scanTokens(String sourceText) {
        List<Token> tokens = new ArrayList<>();
        source = new Source(sourceText);
        while (!source.atEndOfSource()) {
            source.startLexeme();
            char c = source.readChar();
            Token token = scanToken(c);
            if (nonNull(token)){
                tokens.add(token);
            }
        }

        tokens.add(new Token(EOF, "", null, source.getCurrentLine()));
        return tokens;
    }

    private static Token scanToken(char c) {
        return switch (c) {
            case '(' -> createToken(LEFT_PAREN);
            case ')' -> createToken(RIGHT_PAREN);
            case '{' -> createToken(LEFT_BRACE);
            case '}' -> createToken(RIGHT_BRACE);
            case ',' -> createToken(COMMA);
            case '.' -> createToken(DOT);
            case '-' -> createToken(MINUS);
            case '+' -> createToken(PLUS);
            case ';' -> createToken(SEMICOLON);
            case '*' -> createToken(STAR);
            case '!' -> createToken(source.matchChar('=') ? BANG_EQUAL : BANG);
            case '=' -> createToken(source.matchChar('=') ? EQUAL_EQUAL : EQUAL);
            case '<' -> createToken(source.matchChar('=') ? LESS_EQUAL : LESS);
            case '>' -> createToken(source.matchChar('=') ? GREATER_EQUAL : GREATER);
            case ' ', '\r', '\t', '\n' -> null; // Ignore whitespace
            case '/' -> handleComment();
            case '"' -> handleString();
            default -> handleDefault(c);
        };
    }

    private static Token handleDefault(char c) {
        if (isDigit(c)) return handleNumber();
        else if (isAlpha(c)) return handleIdentifier();
        else {
            Lox.error(source.getCurrentLine(), "Unexpected character");
            return null;
        }
    }

    private static Token handleIdentifier() {
        while (isAlphaNumeric(source.peek()))
            source.readChar();

        String text = source.readLexeme();
        TokenType tokenType = TokenType.getKeyword(text);
        return createToken(tokenType);
    }

    private static Token handleNumber() {
        while (isDigit(source.peek()))
            source.readChar();

        if (source.peek() == '.' && isDigit(source.peekNext())) {
            source.readChar();

            while (isDigit(source.peek()))
                source.readChar();
        }

        return createToken(NUMBER, Double.parseDouble(source.readLexeme()));
    }

    private static Token handleComment() {
        if (!source.matchChar('/')) {
            return createToken(SLASH);
        }

        while (source.peek() != '\n' && !source.atEndOfSource()) {
            source.readChar(); // Advance until the end of the line, ignoring commented-out code.
        }
        return null;
    }

    private static Token handleString() {

        while (source.peek() != '"' && !source.atEndOfSource()) {

            source.readChar();
        }

        if (source.atEndOfSource()) {
            Lox.error(source.getCurrentLine(), "Unterminated string.");
            return null;
        }

        // The closing ".
        source.readChar();

        // Trim the surrounding quotes
        String lexeme = source.readLexeme();
        String value = lexeme.substring(1, lexeme.length() - 1);
        return createToken(STRING, value);
    }

    private static Token createToken(TokenType type) {
        return createToken(type, null);
    }

    private static Token createToken(TokenType type, Object literal) {
        String text = source.readLexeme();
        return new Token(type, text, literal, source.getCurrentLine());
    }
}
