package org.example.symbol;

public class Symbol {
    private String name;       // Nombre del identificador
    private SymbolType type;   // Tipo del símbolo
    private Object value;      // Valor (opcional)
    private boolean isFunction; // Si es una función
    private int line;          // Línea donde se declaró
    private int column;        // Columna donde se declaró

    public Symbol(String name, SymbolType type, boolean isFunction, int line, int column) {
        this.name = name;
        this.type = type;
        this.isFunction = isFunction;
        this.line = line;
        this.column = column;
    }

    // Getters y setters
    public String getName() { return name; }
    public SymbolType getType() { return type; }
    public void setType(SymbolType type) { this.type = type; }
    public Object getValue() { return value; }
    public void setValue(Object value) { this.value = value; }
    public boolean isFunction() { return isFunction; }
    public int getLine() { return line; }
    public int getColumn() { return column; }

    @Override
    public String toString() {
        return String.format("%s (%s) %s en línea %d, columna %d",
                name, type, (isFunction ? "[función]" : ""), line, column);
    }
}