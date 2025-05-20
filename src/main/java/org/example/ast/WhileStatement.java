package org.example.ast;

import org.example.symbol.SymbolTable;
import java.util.List;

public class WhileStatement extends Statement {
    private Expression condition;
    private List<Statement> body;

    public WhileStatement(Expression condition, List<Statement> body) {
        this.condition = condition;
        this.body = body;
    }

    // Método para manejar la ejecución y los ámbitos
    public void execute(SymbolTable symbolTable) {
        // En un compilador real, esto sería parte de la generación de código
        symbolTable.enterScope();  // Crear ámbito para el bloque while

        // Lógica del bucle while
        // En un análisis semántico solo verificaríamos los tipos

        // Ejecutar el cuerpo (para análisis semántico)
        for (Statement stmt : body) {
            // Ejecutar cada declaración
        }

        symbolTable.exitScope();  // Cerrar ámbito
    }
}