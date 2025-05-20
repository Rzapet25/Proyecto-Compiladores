package org.example.ast;

import org.example.symbol.SymbolTable;
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

    // Método para manejar la ejecución y los ámbitos
    public void execute(SymbolTable symbolTable) {
        // Evaluar la condición
        boolean conditionMet = evaluateCondition(condition);

        if (conditionMet) {
            symbolTable.enterScope();  // Crear ámbito para el bloque then
            executeStatements(thenBody, symbolTable);
            symbolTable.exitScope();   // Cerrar ámbito del bloque then
        } else {
            // Verificar cláusulas elseif
            boolean elseIfConditionMet = false;

            for (ElseIfClause elseIfClause : elseIfClauses) {
                if (evaluateCondition(elseIfClause.getCondition())) {
                    symbolTable.enterScope();  // Crear ámbito para el elseif
                    executeStatements(elseIfClause.getBody(), symbolTable);
                    symbolTable.exitScope();   // Cerrar ámbito del elseif
                    elseIfConditionMet = true;
                    break;
                }
            }

            // Si hay else y ninguna condición se cumplió
            if (!conditionMet && !elseIfConditionMet && !elseBody.isEmpty()) {
                symbolTable.enterScope();  // Crear ámbito para el else
                executeStatements(elseBody, symbolTable);
                symbolTable.exitScope();   // Cerrar ámbito del else
            }
        }
    }

    // Métodos auxiliares
    private boolean evaluateCondition(Expression condition) {
        // Aquí implementarías la evaluación de la condición
        // Por ahora devuelve true para simplificar
        return true;
    }

    private void executeStatements(List<Statement> statements, SymbolTable symbolTable) {
        for (Statement stmt : statements) {
            // Aquí ejecutarías cada declaración
            // Por ejemplo, si stmt es un IfStatement, llamarías a su método execute
        }
    }
}