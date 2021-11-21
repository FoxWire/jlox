package com.craftinginterpreters.lox;

import java.util.List;
import java.util.Map;

// todo: add static methods that can be called directly on the  class itself.
public class LoxClass implements LoxCallable{

    private final String name;
    private final Map<String, LoxFunction> methods;

    public LoxClass(String name, Map<String, LoxFunction> methods) {
        this.name = name;
        this.methods = methods;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        LoxInstance instance = new LoxInstance(this);
        LoxFunction initializer = findMethod("init");
        if (initializer != null){
            initializer.bind(instance).call(interpreter, arguments);
        }
        return instance;
    }

    @Override
    public int arity() {
        LoxFunction init = findMethod("init");
        if (init == null) return 0;
        return init.arity();
    }

    public String getName() {
        return name;
    }

    public LoxFunction findMethod(String name) {
        if (methods.containsKey(name)){
            return methods.get(name);
        }
        return null;
    }
}
