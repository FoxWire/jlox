package com.craftinginterpreters.lox;

import java.util.Objects;

public record Token(TokenType type, String lexeme, Object literal, int line) {

    @Override
    public String toString() {
        return "<" + type + " " + lexeme + " " + literal + ">";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return type == token.type && Objects.equals(lexeme, token.lexeme);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, lexeme, literal);
    }
}
