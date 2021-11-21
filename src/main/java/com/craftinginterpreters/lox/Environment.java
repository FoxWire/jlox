package com.craftinginterpreters.lox;

import java.util.HashMap;
import java.util.Map;

public class Environment {
    final Environment enclosing;

    private final Map<String, Object> values;

    public Environment() {
        values = new HashMap<>();
        enclosing = null;
    }

    public Environment(Environment enclosing) {
        values = new HashMap<>();
        this.enclosing = enclosing;
    }

    public void define(String name, Object value) {
        values.put(name, value);
    }

    public Object get(Token name) {
        String lexeme = name.lexeme();
        if (values.containsKey(lexeme)) {
            return values.get(lexeme);
        }

        if (hasEnclosingEnvironment())
            return enclosing.get(name);

        throw new RuntimeError(name, "Undefined variable '" + lexeme + "'.");
    }

    public void assign(Token name, Object value) {
        String lexeme = name.lexeme();
        if (values.containsKey(lexeme)) {
            values.put(lexeme, value);
            return;
        }

        if (hasEnclosingEnvironment()){
            enclosing.assign(name, value);
            return;
        }

        throw new RuntimeError(name, "Undefined variable '" + lexeme + "'.");

    }

    private boolean hasEnclosingEnvironment() {
        return enclosing != null;
    }

    public Object getAt(int distance, String name) {
        return ancestor(distance).values.get(name);
    }

    private Environment ancestor(int distance) {
        Environment environment = this;
        for (int i = 0; i < distance; i++) {
            environment = environment.enclosing;
        }
        return environment;
    }

    public void assignAt(int distance, Token name, Object value) {
        ancestor(distance).values.put(name.lexeme(), value);
    }

}
