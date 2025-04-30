package org.example.ast;


import org.example.tac.TACGenerator;

public class LiteralExpression extends Expression {
    private Object value;

    public LiteralExpression(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }
    @Override
    public String generateTAC(TACGenerator generator) {
        // Para literales simples como números, podemos retornar directamente el valor
        // sin crear un temporal separado
        if (value instanceof String) {
            return "\"" + value + "\"";
        } else {
            return value.toString();
        }


    }


}