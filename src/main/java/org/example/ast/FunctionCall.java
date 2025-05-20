package org.example.ast;

import java.util.List;

public class FunctionCall extends Expression {
    private String name;
    private List<Expression> arguments;

    public FunctionCall(String name, List<Expression> arguments) {
        this.name = name;
        this.arguments = arguments;
    }

    // Eliminar el método generateTAC
}