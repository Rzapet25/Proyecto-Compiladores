package org.example.ast;

import org.example.tac.TACGenerator;

public class VarDeclaration extends Statement {
    private String id;
    private Expression value;

    public VarDeclaration(String id, Expression value) {
        this.id = id;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public Expression getValue() {
        return value;
    }

    @Override
    public String generateTAC(TACGenerator generator) {
        String valueTemp = value.generateTAC(generator);
        return id + " = " + valueTemp + "\n";
    }
}