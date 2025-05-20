package org.example.ast;

public class LiteralExpression extends Expression {
    private Object value;

    public LiteralExpression(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    // Eliminar el método generateTAC
}