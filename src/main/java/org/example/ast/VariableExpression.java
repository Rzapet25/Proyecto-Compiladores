package org.example.ast;

import org.example.tac.TACGenerator;

public class VariableExpression extends Expression {
    private String name;

    public VariableExpression(String name) {
        this.name = name;
    }

    @Override
    public String generateTAC(TACGenerator generator) {
        return name;
    }
}