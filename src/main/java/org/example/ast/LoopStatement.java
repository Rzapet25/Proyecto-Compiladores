package org.example.ast;

import org.example.tac.TACGenerator;

import java.util.List;

public class LoopStatement extends Statement {
    private VarDeclaration initialization;
    private Expression condition;
    private Expression increment;
    private List<Statement> body;

    public LoopStatement(VarDeclaration initialization, Expression condition,
                         Expression increment, List<Statement> body) {
        this.initialization = initialization;
        this.condition = condition;
        this.increment = increment;
        this.body = body;
    }

    @Override
    public String generateTAC(TACGenerator generator) {
        StringBuilder code = new StringBuilder();

        // Initialization
        code.append(initialization.generateTAC(generator));

        String startLabel = generator.generateLabel();
        String bodyLabel = generator.generateLabel();
        String endLabel = generator.generateLabel();

        // Start of loop
        code.append("label ").append(startLabel).append("\n");

        // Condition
        String condTemp = condition.generateTAC(generator);
        code.append("if ").append(condTemp).append(" goto ").append(bodyLabel).append("\n");
        code.append("goto ").append(endLabel).append("\n");

        // Body
        code.append("label ").append(bodyLabel).append("\n");
        for (Statement stmt : body) {
            code.append(stmt.generateTAC(generator));
        }

        // Increment
        String incrTemp = increment.generateTAC(generator);

        // Go back to condition
        code.append("goto ").append(startLabel).append("\n");

        // End
        code.append("label ").append(endLabel).append("\n");

        return code.toString();
    }
}