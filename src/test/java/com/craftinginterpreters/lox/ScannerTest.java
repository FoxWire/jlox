package com.craftinginterpreters.lox;

import com.craftinginterpreters.lox.scanner.Scanner;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
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
            "if (condition) {\nprint \"yes\";\n} else {\nprint \"no\";\n}",
            "var a = 1; while (a < 10) { print a; a = a + 1; }",
            "for (var a = 1; a < 10; a = a + 1) { print a;",
            "var average = (min + max) / 2",
            "if (monday) print \"Ugh, already?\";",
            "fun isOdd(n) { if (n == 0) return false; return isEven(n - 1); } " +
                    "fun isEven(n) { if (n == 0) return true; return isOdd(n - 1); }",
            "class Saxophone { play() { print \"Careless Whisper\"; } } class GolfClub { play() { print \"Fore!\"; } } fun playIt(thing) { thing.play(); }",
            "var a = 0; var temp; for (var b = 1; a < 10000; b = temp + b) { print a; temp = a; a = b;",
            "fun add(a, b, c) { print a + b + c; }",
            "fun count(n) { if (n > 1) count(n - 1); print n; } count(3);",
            "fun add(a, b, c) { print a + b + c; } add(1, 2, 3);",
            "fun add(a, b) { print a + b; } print add;",
            "fun procedure() { print \"don't return anything\"; } var result = procedure(); print result;",
            "fun count(n) { while (n < 100) { if (n == 3) return n; print n; n = n + 1; } } count(1);",
            "fun fib(n) { if (n <= 1) return n; return fib(n - 2) + fib(n - 1); } for (var i = 0; i < 20; i = i + 1) { print fib(i); }",
            "fun makeCounter() { var i = 0; fun count() { i = i + 1; print i; } return count; } var counter = makeCounter(); counter(); counter();",
            "var a = \"outer\"; { var a = \"inner\"; print a; }",
            "var a = \"global\"; { fun showA() { print a; } showA(); var a = \"block\"; showA(); }",
            "var a = \"outer\"; { var a = a; }",
            "class Breakfast { cook() { print \"Eggs a-fryin'!\"; } serve(who) { print \"Enjoy your breakfast, \" + who + \".\"; } }",
            "class DevonshireCream { serveOn() { return \"Scones\"; } } print DevonshireCream;",
            "class Box {} fun notMethod(argument) { print \"called function with \" + argument; } var box = Box(); box.function = notMethod; box.function(\"argument\");",
            "class Person { sayName() { print this.name; } } var jane = Person(); jane.name = \"Jane\"; var method = jane.sayName; method();",
            "class Person { sayName() { print this.name; } } var jane = Person(); jane.name = \"Jane\"; var bill = Person(); bill.name = \"Bill\"; bill.sayName = jane.sayName; bill.sayName();",
            "class Bacon { eat() { print \"Crunch crunch crunch!\"; } } Bacon().eat();",
            "class Egotist { speak() { print this; } } var method = Egotist().speak; method();",
            "class Cake { taste() { var adjective = \"delicious\"; print \"The \" + this.flavor + \" cake is \" + adjective + \"!\"; } } var cake = Cake(); cake.flavor = \"German chocolate\"; cake.taste();",
            "class Thing { getCallback() { fun localFunction() { print this; } return localFunction; } } var callback = Thing().getCallback(); callback();",
            "class Foo { init() { print this; } } var foo = Foo(); print foo.init();",
            "class Foo { init() { return \"something else\"; } }",
            "class Doughnut { cook() { print \"Fry until golden brown.\"; } }",
            "class BostonCream < Doughnut {} BostonCream().cook();",
            "class Doughnut { cook() { print \"Fry until golden brown.\"; } }",
            "class BostonCream < Doughnut { cook() { super.cook(); print \"Pipe full of custard and coat with chocolate.\"; } } BostonCream().cook();",
            "class A { method() { print \"A method\"; } } class B < A { method() { print \"B method\"; } test() { super.method(); } } class C < B {} C().test();",
            "class Eclair { cook() { super.cook(); print \"Pipe full of crème pâtissière.\"; } }"
    })
    public void testLexemesCanBeUsedToRebuildSource(String source) {

        List<Token> tokens = Scanner.scanTokens(source);

        String result = tokens.stream()
                .map(Token::lexeme)
                .collect(joining());

        Assertions.assertThat(removeWhitespace(result))
                .isEqualTo(removeWhitespace(source));
    }

    private String removeWhitespace(String string) {
        return string.replaceAll("[\\s\\xA0]", "");
    }


    @ParameterizedTest
    @ValueSource(strings = {
            "var a = 0; // this is a comment\n",
            "var a = 0; // this is a comment"
    })
    public void testCommentsAreIgnored(String source) {

        List<Token> tokens = Scanner.scanTokens(source);

        String result = tokens.stream()
                .map(Token::lexeme)
                .collect(joining());

        Assertions.assertThat(removeWhitespace(result))
                .isEqualTo(removeWhitespace("var a = 0;"));
    }
}