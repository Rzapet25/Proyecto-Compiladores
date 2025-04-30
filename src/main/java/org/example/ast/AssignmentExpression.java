package org.example.ast;

import org.example.tac.TACGenerator;

public class AssignmentExpression extends Expression {
    private String id;
    private Expression value;

    public AssignmentExpression(String id, Expression value) {
        this.id = id;
        this.value = value;
    }

    @Override
    public String generateTAC(TACGenerator generator) {
        String valueTemp = value.generateTAC(generator);
        generator.addInstruction(id + " = " + valueTemp);
        return id;
    }
}