package com.craftinginterpreters.lox;

import com.craftinginterpreters.lox.scanner.Scanner;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static java.util.stream.Collectors.joining;

class ScannerTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "1234;",
            "12.34;",
            "add + me;",
            "subtract - me;",
            "multiply * me;",
            "divide / me;",
            "-negateMe;",
            "less < than;",
            "lessThan <= orEqual;",
            "greater > than;",
            "greaterThan >= orEqual;",
            "1 == 2;",
            "false or false",
            "true and false;",
            "if (condition) { print \"yes\"; } else { print \"no\"; }",
            "var a = 1; while (a < 10) { print a; a = a + 1; }",
            "for (var a = 1; a < 10; a = a + 1) { print a;",
            "var average = (min + max) / 2",
    })
    public void testLexemesCanBeUsedToRebuildSource(String source){

        List<Token> tokens = new Scanner(source).scanTokens();

        String result = tokens.stream()
                .map(Token::lexeme)
                .collect(joining());

        String whitespaceRemoved = source.replace(" ", "");
        Assertions.assertThat(result).isEqualTo(whitespaceRemoved);
    }
}