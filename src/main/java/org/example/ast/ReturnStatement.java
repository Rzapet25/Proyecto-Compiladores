package org.example.ast;

public class ReturnStatement extends Statement {
    private Expression value;

    public ReturnStatement(Expression value) {
        this.value = value;
    }

    // Eliminar el método generateTAC
}