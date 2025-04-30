package org.example.ast;

import org.example.tac.TACGenerator;

import java.util.List;

public class PrintStatement extends Statement {
    private List<Expression> expressions;

    public PrintStatement(List<Expression> expressions) {
        this.expressions = expressions;
    }

    @Override
    public String generateTAC(TACGenerator generator) {
        StringBuilder code = new StringBuilder();

        for (Expression expr : expressions) {
            String temp = expr.generateTAC(generator);
            code.append("print ").append(temp).append("\n");
        }

        return code.toString();
    }
}