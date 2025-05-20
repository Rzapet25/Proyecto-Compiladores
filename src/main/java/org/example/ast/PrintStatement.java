package org.example.ast;

import java.util.List;

public class PrintStatement extends Statement {
    private List<Expression> expressions;

    public PrintStatement(List<Expression> expressions) {
        this.expressions = expressions;
    }

    // No necesita implementar generateTAC
}