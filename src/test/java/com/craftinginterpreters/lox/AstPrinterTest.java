package com.craftinginterpreters.lox;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class AstPrinterTest {


    @Test
    public void testPrinter() {

        Expr expression = new Expr.Binary(
                new Expr.Unary(
                        new Token(TokenType.MINUS, "-", null, 1),
                        new Expr.Literal(123)),
                new Token(TokenType.STAR, "*", null, 1),
                new Expr.Grouping(
                        new Expr.Literal(45.67)));

        String result = new AstPrinter().print(expression);

        Assertions.assertThat(result).isEqualTo("(* (- 123) (group 45.67))");
    }
}