package org.example.ast;

import org.example.tac.TACGenerator;

public class ExpressionStatement extends Statement {
    private Expression expression;

    public ExpressionStatement(Expression expression) {
        this.expression = expression;
    }

    @Override
    public String generateTAC(TACGenerator generator) {
        return expression.generateTAC(generator);
    }
}