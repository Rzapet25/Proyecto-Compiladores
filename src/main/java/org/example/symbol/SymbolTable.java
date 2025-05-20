package org.example.symbol;

import java.util.*;
import org.example.error.ErrorCompilacion;

public class SymbolTable {
    private List<Map<String, Symbol>> scopes;
    private List<ErrorCompilacion> errors;

    public SymbolTable() {
        this.scopes = new ArrayList<>();
        this.errors = new ArrayList<>();
        // Iniciar con ámbito global
        enterScope();
    }

    /**
     * Entra en un nuevo ámbito
     */
    public void enterScope() {
        scopes.add(new HashMap<>());
    }

    /**
     * Sale del ámbito actual
     */
    public void exitScope() {
        if (scopes.size() > 1) { // Siempre mantener al menos el ámbito global
            scopes.remove(scopes.size() - 1);
        }
    }

    /**
     * Define un nuevo símbolo en el ámbito actual
     */
    public boolean define(String name, SymbolType type, boolean isFunction, int line, int column) {
        Map<String, Symbol> currentScope = scopes.get(scopes.size() - 1);

        if (currentScope.containsKey(name)) {
            Symbol existing = currentScope.get(name);
            errors.add(new ErrorCompilacion("Semántico", line, column,
                    "Redefinición de '" + name + "', ya definido en línea " + existing.getLine()));
            return false;
        }

        currentScope.put(name, new Symbol(name, type, isFunction, line, column));
        return true;
    }

    /**
     * Busca un símbolo en todos los ámbitos, comenzando por el más interior
     */
    public Symbol resolve(String name, int line, int column) {
        // Buscar desde el ámbito más interno hacia afuera
        for (int i = scopes.size() - 1; i >= 0; i--) {
            Symbol symbol = scopes.get(i).get(name);
            if (symbol != null) {
                return symbol;
            }
        }

        errors.add(new ErrorCompilacion("Semántico", line, column,
                "Variable no definida: '" + name + "'"));
        return null;
    }

    /**
     * Verifica si un símbolo existe en algún ámbito
     */
    public boolean exists(String name) {
        for (int i = scopes.size() - 1; i >= 0; i--) {
            if (scopes.get(i).containsKey(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Obtiene todos los errores acumulados
     */
    public List<ErrorCompilacion> getErrors() {
        return errors;
    }

    /**
     * Obtiene todos los símbolos del ámbito actual
     */
    public List<Symbol> getCurrentScopeSymbols() {
        return new ArrayList<>(scopes.get(scopes.size() - 1).values());
    }

    /**
     * Obtiene todos los símbolos de todos los ámbitos
     */
    public List<Symbol> getAllSymbols() {
        List<Symbol> allSymbols = new ArrayList<>();
        for (Map<String, Symbol> scope : scopes) {
            allSymbols.addAll(scope.values());
        }
        return allSymbols;
    }
}
