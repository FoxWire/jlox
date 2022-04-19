package com.craftinginterpreters.lox;

import com.craftinginterpreters.lox.scanner.ScannerUtil;

import java.util.ArrayList;
import java.util.List;

import static com.craftinginterpreters.lox.TokenType.*;

/**
 * This will take an ast (list of statements) and
 * construct a list of tokens.
 * Used for tests.
 */
public class Deparser implements Expr.Visitor<Object>, Stmt.Visitor<Void> {

    private final List<Token> tokens;

    public Deparser() {
        tokens = new ArrayList<>();
    }

    public List<Token> deparse(List<Stmt> statements) {

        for (Stmt statement : statements) {
            statement.accept(this);
            if (
            !(statement instanceof Stmt.If)
            && !(statement instanceof Stmt.While)
            && !(statement instanceof Stmt.Function)

            ) {
                tokens.add(new Token(SEMICOLON, ";", null, 0));
            }
        }
        tokens.add(new Token(EOF, "", null, 0));
        return tokens;
    }

    @Override
    public String visitAssignExpr(Expr.Assign expr) {
        tokens.add(expr.name);
        tokens.add(new Token(EQUAL, "=", null, 0));
        expr.value.accept(this);
        return null;
    }

    @Override
    public String visitBinaryExpr(Expr.Binary expr) {
        expr.left.accept(this);
        tokens.add(expr.operator);
        expr.right.accept(this);
        return null;
    }

    @Override
    public String visitCallExpr(Expr.Call expr) {
        expr.callee.accept(this);
        tokens.add(new Token(LEFT_PAREN, "(", null, 0));
        for (Expr argument : expr.arguments) {
            argument.accept(this);
        }
        tokens.add(new Token(RIGHT_PAREN, ")", null, 0));
        return null;
    }

    @Override
    public String visitGetExpr(Expr.Get expr) {
        expr.object.accept(this);
        tokens.add(expr.name);
        return null;
    }

    @Override
    public String visitGroupingExpr(Expr.Grouping expr) {
        tokens.add(new Token(LEFT_PAREN, "(", null, 0));
        expr.expression.accept(this);
        tokens.add(new Token(RIGHT_PAREN, ")", null, 0));
        return null;
    }

    @Override
    public String visitLiteralExpr(Expr.Literal expr) {

        String value = expr.value.toString();
        if (ScannerUtil.isDigit(value.charAt(0))) {

            if (value.endsWith(".0")) {
                String asInt = value.substring(0, value.length() - 2);
                tokens.add(new Token(NUMBER, asInt, value, 0));
            } else {
                tokens.add(new Token(NUMBER, value, value, 0));
            }
        } else if (value.equals("true")){
            tokens.add(new Token(TRUE, "true", null, 0));
        } else if (value.equals("false")){
            tokens.add(new Token(FALSE, "false", null, 0));
        } else {
            tokens.add(new Token(STRING, "\"" +  value + "\"", value, 0));
        }

        return null;
    }

    @Override
    public String visitLogicalExpr(Expr.Logical expr) {
        expr.left.accept(this);
        tokens.add(expr.operator);
        expr.right.accept(this);
        return null;
    }

    @Override
    public String visitSetExpr(Expr.Set expr) {
        expr.object.accept(this);
        tokens.add(expr.name);
        expr.value.accept(this);
        return null;
    }

    @Override
    public String visitSuperExpr(Expr.Super expr) {
        tokens.add(expr.keyword);
        tokens.add(expr.method);
        return null;
    }

    @Override
    public String visitThisExpr(Expr.This expr) {
        tokens.add(expr.keyword);
        return null;
    }

    @Override
    public String visitUnaryExpr(Expr.Unary expr) {
        tokens.add(expr.operator);
        expr.right.accept(this);
        return null;
    }

    @Override
    public String visitVariableExpr(Expr.Variable expr) {
        tokens.add(expr.name);
        return null;
    }

    @Override
    public Void visitBlockStmt(Stmt.Block stmt) {
        for (Stmt statement : stmt.statements) {
            statement.accept(this);
            tokens.add(new Token(SEMICOLON, ";", null, 0));
        }
        return null;
    }

    @Override
    public Void visitClassStmt(Stmt.Class stmt) {
        tokens.add(new Token(CLASS, "class", null, 0));
        tokens.add(stmt.name);
        if (stmt.superClass != null) {
            stmt.superClass.accept(this);
        }
        tokens.add(new Token(LEFT_BRACE, "{", null, 0));
        for (Stmt.Function method : stmt.methods) {
            method.accept(this);
        }
        tokens.add(new Token(RIGHT_BRACE, "}", null, 0));
        return null;
    }

    @Override
    public Void visitExpressionStmt(Stmt.Expression stmt) {
        stmt.expression.accept(this);
        return null;
    }

    @Override
    public Void visitFunctionStmt(Stmt.Function stmt) {
        tokens.add(new Token(FUN, "fun", null, 0));
        tokens.add(stmt.name);
        tokens.add(new Token(LEFT_PAREN, "(", null, 0));

        if (!stmt.params.isEmpty()){
            for (int i = 0; i < stmt.params.size() - 1; i++) {
                tokens.add(stmt.params.get(i));
                tokens.add(new Token(COMMA, ",", null, 0));
            }
            tokens.add(stmt.params.get(stmt.params.size() - 1));
        }

        tokens.add(new Token(RIGHT_PAREN, ")", null, 0));
        tokens.add(new Token(LEFT_BRACE, "{", null, 0));
        for (Stmt stmt1 : stmt.body) {
            stmt1.accept(this);
        }
        tokens.add(new Token(RIGHT_BRACE, "}", null, 0));
        return null;
    }

    @Override
    public Void visitIfStmt(Stmt.If stmt) {
        tokens.add(new Token(IF, "if", null, 0));
        tokens.add(new Token(LEFT_PAREN, "(", null, 0));
        stmt.condition.accept(this);
        tokens.add(new Token(RIGHT_PAREN, ")", null, 0));

        tokens.add(new Token(LEFT_BRACE, "{", null, 0));
        stmt.thenBranch.accept(this);

        tokens.add(new Token(RIGHT_BRACE, "}", null, 0));

        if (stmt.elseBranch != null) {

            tokens.add(new Token(ELSE, "else", null, 0));
            tokens.add(new Token(LEFT_BRACE, "{", null, 0));
            stmt.elseBranch.accept(this);
            tokens.add(new Token(RIGHT_BRACE, "}", null, 0));

        }

        return null;
    }

    @Override
    public Void visitPrintStmt(Stmt.Print stmt) {
        tokens.add(new Token(PRINT, "print", null, 0));
        stmt.expression.accept(this);
        return null;
    }

    @Override
    public Void visitReturnStmt(Stmt.Return stmt) {
        tokens.add(stmt.keyword);
        stmt.value.accept(this);
        return null;
    }

    @Override
    public Void visitVarStmt(Stmt.Var stmt) {
        tokens.add(new Token(VAR, "var", null, 0));
        tokens.add(stmt.name);
        if (stmt.initializer != null) {
            tokens.add(new Token(EQUAL, "=", null, 0));
            stmt.initializer.accept(this);
        }
        return null;
    }

    @Override
    public Void visitWhileStmt(Stmt.While stmt) {
        tokens.add(new Token(WHILE, "while", null, 0));
        tokens.add(new Token(LEFT_PAREN, "(", null, 0));
        stmt.condition.accept(this);
        tokens.add(new Token(RIGHT_PAREN, ")", null, 0));
        tokens.add(new Token(LEFT_BRACE, "{", null, 0));
        stmt.body.accept(this);
        tokens.add(new Token(RIGHT_BRACE, "}", null, 0));
        return null;
    }
}
