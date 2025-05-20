package org.example.ast;

public class UnaryExpression extends Expression {
    private String operator;
    private Expression operand;

    public UnaryExpression(String operator, Expression operand) {
        this.operator = operator;
        this.operand = operand;
    }

    // Eliminar el método generateTAC
}