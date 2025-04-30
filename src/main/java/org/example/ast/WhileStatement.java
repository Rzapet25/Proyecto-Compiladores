package org.example.ast;

import org.example.tac.TACGenerator;

import java.util.List;

public class WhileStatement extends Statement {
    private Expression condition;
    private List<Statement> body;

    public WhileStatement(Expression condition, List<Statement> body) {
        this.condition = condition;
        this.body = body;
    }

    @Override
    public String generateTAC(TACGenerator generator) {
        StringBuilder code = new StringBuilder();
        String startLabel = generator.generateLabel();
        String endLabel = generator.generateLabel();

        // Etiqueta de inicio
        code.append(startLabel).append(":\n");

        // Evaluar condición
        String condTemp = condition.generateTAC(generator);

        // Instrucción if
        code.append("if ").append(condTemp).append(" == 0 goto ").append(endLabel).append("\n");

        // Cuerpo del bucle - ¡IMPORTANTE! Asegúrate de que cada instrucción termine con \n
        for (Statement stmt : body) {
            String stmtCode = stmt.generateTAC(generator);
            // Si el código generado no termina con \n, añadirlo
            if (!stmtCode.endsWith("\n")) {
                stmtCode += "\n";
            }
            code.append(stmtCode);
        }

        // Salto al inicio - AQUÍ ESTABA EL ERROR
        code.append("goto ").append(startLabel).append("\n");

        // Etiqueta final
        code.append(endLabel).append(":\n");

        return code.toString();
    }
}