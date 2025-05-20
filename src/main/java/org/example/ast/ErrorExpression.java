package org.example.ast;

public class ErrorExpression extends Expression {
    private String message;

    public ErrorExpression(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}