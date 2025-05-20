package org.example.error;

public class ErrorCompilacion {
    private String tipo;        // "Léxico" o "Sintáctico"
    private int linea;
    private int columna;
    private String mensaje;

    public ErrorCompilacion(String tipo, int linea, int columna, String mensaje) {
        this.tipo = tipo;
        this.linea = linea;
        this.columna = columna;
        this.mensaje = mensaje;
    }

    // Getters y setters
    public String getTipo() { return tipo; }
    public int getLinea() { return linea; }
    public int getColumna() { return columna; }
    public String getMensaje() { return mensaje; }

    @Override
    public String toString() {
        return "Error " + tipo + " en línea " + (linea + 1) + ", columna " + (columna + 1) + ": " + mensaje;
    }
}