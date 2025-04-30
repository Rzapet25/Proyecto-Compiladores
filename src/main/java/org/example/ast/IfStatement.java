package org.example.ast;

import org.example.tac.TACGenerator;

import java.util.List;

public class IfStatement extends Statement {
    private Expression condition;
    private List<Statement> thenBody;
    private List<ElseIfClause> elseIfClauses;
    private List<Statement> elseBody;

    public IfStatement(Expression condition, List<Statement> thenBody,
                       List<ElseIfClause> elseIfClauses, List<Statement> elseBody) {
        this.condition = condition;
        this.thenBody = thenBody;
        this.elseIfClauses = elseIfClauses;
        this.elseBody = elseBody;
    }

    @Override
    public String generateTAC(TACGenerator generator) {
        StringBuilder code = new StringBuilder();

        String condTemp = condition.generateTAC(generator);
        String thenLabel = generator.generateLabel();
        String elseIfLabel = generator.generateLabel();
        String elseLabel = generator.generateLabel();
        String endLabel = generator.generateLabel();

        // If condition
        code.append("if ").append(condTemp).append(" goto ").append(thenLabel).append("\n");

        // Jump to else-if or else
        if (!elseIfClauses.isEmpty()) {
            code.append("goto ").append(elseIfLabel).append("\n");
        } else if (!elseBody.isEmpty()) {
            code.append("goto ").append(elseLabel).append("\n");
        } else {
            code.append("goto ").append(endLabel).append("\n");
        }

        // Then body
        code.append("label ").append(thenLabel).append("\n");
        for (Statement stmt : thenBody) {
            code.append(stmt.generateTAC(generator));
        }
        code.append("goto ").append(endLabel).append("\n");

        // Else-if clauses
        if (!elseIfClauses.isEmpty()) {
            String currentLabel = elseIfLabel;

            for (int i = 0; i < elseIfClauses.size(); i++) {
                ElseIfClause clause = elseIfClauses.get(i);
                String clauseLabel = generator.generateLabel();
                String nextLabel = (i == elseIfClauses.size() - 1) ?
                        (elseBody.isEmpty() ? endLabel : elseLabel) : generator.generateLabel();

                code.append("label ").append(currentLabel).append("\n");
                String elseIfCondTemp = clause.getCondition().generateTAC(generator);
                code.append("if ").append(elseIfCondTemp).append(" goto ").append(clauseLabel).append("\n");
                code.append("goto ").append(nextLabel).append("\n");

                code.append("label ").append(clauseLabel).append("\n");
                for (Statement stmt : clause.getBody()) {
                    code.append(stmt.generateTAC(generator));
                }
                code.append("goto ").append(endLabel).append("\n");

                currentLabel = nextLabel;
            }
        }

        // Else body
        if (!elseBody.isEmpty()) {
            code.append("label ").append(elseLabel).append("\n");
            for (Statement stmt : elseBody) {
                code.append(stmt.generateTAC(generator));
            }
        }

        // End of if statement
        code.append("label ").append(endLabel).append("\n");

        return code.toString();
    }
}