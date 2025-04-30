package org.example.ast;

import org.example.tac.TACGenerator;

public class BinaryExpression extends Expression {
    private Expression left;
    private String operator;
    private Expression right;

    public BinaryExpression(Expression left, String operator, Expression right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

   @Override
   public String generateTAC(TACGenerator generator) {
       // Para expresiones simples donde ambos lados son literales,
       // podemos optimizar y no generar temporales intermedios
       if (left instanceof LiteralExpression && right instanceof LiteralExpression) {
           String resultTemp = generator.generateTemp();
           String leftValue = ((LiteralExpression) left).getValue().toString();
           String rightValue = ((LiteralExpression) right).getValue().toString();
           generator.addInstruction(resultTemp + " = " + leftValue + " " + operator + " " + rightValue);
           return resultTemp;
       } else {
           // Comportamiento normal para expresiones más complejas
           String leftTemp = left.generateTAC(generator);
           String rightTemp = right.generateTAC(generator);
           String resultTemp = generator.generateTemp();
           generator.addInstruction(resultTemp + " = " + leftTemp + " " + operator + " " + rightTemp);
           return resultTemp;
       }
   }
}