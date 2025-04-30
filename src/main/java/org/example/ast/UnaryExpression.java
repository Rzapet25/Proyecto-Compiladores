package org.example.ast;

import org.example.tac.TACGenerator;

public class UnaryExpression extends Expression {
    private String operator;
    private Expression operand;

    public UnaryExpression(String operator, Expression operand) {
        this.operator = operator;
        this.operand = operand;
    }

    @Override
    public String generateTAC(TACGenerator generator) {
        String operandTemp = operand.generateTAC(generator);
        String resultTemp = generator.generateTemp();

        generator.addInstruction(resultTemp + " = " + operator + " " + operandTemp);

        return resultTemp;
    }
}