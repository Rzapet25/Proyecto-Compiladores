package org.example.ast;

import org.example.symbol.SymbolTable;
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

    // Método para manejar la ejecución y los ámbitos
    public void execute(SymbolTable symbolTable) {
        symbolTable.enterScope();  // Crear ámbito para el loop

        // Ejecutar la inicialización
        // initialization.execute(symbolTable);

        // En un compilador real, aquí implementarías la lógica del bucle
        // Para análisis semántico solo necesitamos verificar los tipos

        // Ejecutar el cuerpo (para análisis semántico)
        for (Statement stmt : body) {
            // Ejecutar cada declaración
        }

        symbolTable.exitScope();  // Cerrar ámbito
    }
}