package org.example.ast;

import java.util.List;

public class ElseIfClause {
    private Expression condition;
    private List<Statement> body;

    public ElseIfClause(Expression condition, List<Statement> body) {
        this.condition = condition;
        this.body = body;
    }

    public Expression getCondition() {
        return condition;
    }

    public List<Statement> getBody() {
        return body;
    }
}
