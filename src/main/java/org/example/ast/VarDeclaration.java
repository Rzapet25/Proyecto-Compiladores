package org.example.ast;

public class VarDeclaration extends Statement {
    private String name;
    private Expression initialValue;

    public VarDeclaration(String name, Expression initialValue) {
        this.name = name;
        this.initialValue = initialValue;
    }

    public String getName() {
        return name;
    }

    public Expression getInitialValue() {
        return initialValue;
    }
}