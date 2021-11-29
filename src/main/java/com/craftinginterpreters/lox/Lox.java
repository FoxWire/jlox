package com.craftinginterpreters.lox;

import com.craftinginterpreters.lox.scanner.Scanner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Lox {

    private static final Interpreter interpreter = new Interpreter();
    private static boolean hadError = false;
    private static boolean hadRuntimeError;

    public static void main(String[] args) throws IOException {
        if (args.length > 1) {
            System.out.println("Usage: jlox [script]");
            return;
        }

        if (args.length == 1){
            runFile(args[0]);
        } else {
            runPrompt();
        }
    }

    private static void runFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()));

        if (hadError)
            System.exit(65);

        if (hadRuntimeError)
            System.exit(70);
    }

    private static void runPrompt() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while (true){
            System.out.println("> ");
            String line = reader.readLine();
            if (line == null)
                break;
            run(line);
            hadError = false;
        }
    }

    private static void run(String source) {
        List<Token> tokens = Scanner.scanTokens(source);

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        // Stop if there was a syntax error.
        if (hadError) return;

        Resolver resolver = new Resolver(interpreter);
        resolver.resolve(statements);

        // Stop if there was a resolution error
        if (hadError) return;

        interpreter.interpret(statements);

    }

    public static void error(int line, String message){
        report(line, "", message);
    }


    public static void error(Token token, String message){
        if (token.type() == TokenType.EOF){
            report(token.line(), " at end", message);
        } else {
            report(token.line(), " at '" + token.lexeme() + "'" + message);
        }
    }

    private static void report(int line, String s) {
    }

    private static void report(int line, String where, String message) {
        System.err.println( "[line " + line + "] Error" + where + ": " + message);
        hadError = true;
    }

    public static void runtimeError(RuntimeError error) {
        System.err.println(error.getMessage() + "\n[line " + error.getToken().line()+ "]");
        hadRuntimeError = true;
    }
}
