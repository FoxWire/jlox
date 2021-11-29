package com.craftinginterpreters.lox;

public enum TokenType {

    // Single-character tokens.
    LEFT_PAREN, RIGHT_PAREN, LEFT_BRACE, RIGHT_BRACE,
    COMMA, DOT, MINUS, PLUS, SEMICOLON, SLASH, STAR,

    // One or two character tokens.
    BANG, BANG_EQUAL,
    EQUAL, EQUAL_EQUAL,
    GREATER, GREATER_EQUAL,
    LESS, LESS_EQUAL,

    // Literals.
    IDENTIFIER, STRING, NUMBER,

    // Keywords.
    AND, CLASS, ELSE, FALSE, FUN, FOR, IF, NIL, OR,
    PRINT, RETURN, SUPER, THIS, TRUE, VAR, WHILE,

    EOF;

    public static TokenType getKeyword(String key){

        return switch (key) {
            case "and" -> AND;
            case "class" -> CLASS;
            case "else" -> ELSE;
            case "false" -> FALSE;
            case "for" -> FOR;
            case "fun" -> FUN;
            case "if" -> IF;
            case "nil" -> NIL;
            case "or" -> OR;
            case "print" -> PRINT;
            case "return" -> RETURN;
            case "super" -> SUPER;
            case "this" -> THIS;
            case "true" -> TRUE;
            case "var" -> VAR;
            case "while" -> WHILE;
            default -> IDENTIFIER;
        };
    }
}
