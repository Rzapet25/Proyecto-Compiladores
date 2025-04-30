package org.example.ast;

import org.example.tac.TACGenerator;

public class ReturnStatement extends Statement {
    private Expression value;

    public ReturnStatement(Expression value) {
        this.value = value;
    }

    @Override
    public String generateTAC(TACGenerator generator) {
        String valueTemp = value.generateTAC(generator);
        return "return " + valueTemp + "\n";
    }
}