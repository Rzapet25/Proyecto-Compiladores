package org.example.ast;

public class AssignmentExpression extends Expression {
    private String id;
    private Expression value;

    public AssignmentExpression(String id, Expression value) {
        this.id = id;
        this.value = value;
    }

}