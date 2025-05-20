package org.example.service;

import org.example.error.ErrorCompilacion;
import org.example.lexer.Lexer;
import org.example.parser.Parser;
import org.example.symbol.Symbol;
import org.springframework.stereotype.Service;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class CompiladorService {

    /**
     * Compila el código proporcionado y devuelve un resultado formateado como String.
     * @param codigo El código fuente a compilar
     * @return Resultado de la compilación como String
     */
    public String compilar(String codigo) {
        try {
            Parser parser = new Parser(null);
            List<ErrorCompilacion> errores = parser.errores;

            Lexer lexer = new Lexer(new StringReader(codigo), parser);
            parser.setScanner(lexer);
            parser.parse();

            // Agregar errores semánticos de la tabla de símbolos
            errores.addAll(parser.symbolTable.getErrors());

            if (errores.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                sb.append("Compilación exitosa. No se encontraron errores.\n\n");

                // Mostrar tabla de símbolos
                sb.append("Tabla de símbolos:\n");
                for (Symbol simbolo : parser.symbolTable.getAllSymbols()) {
                    sb.append("- ").append(simbolo).append("\n");
                }

                return sb.toString();
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("Se encontraron los siguientes errores:\n");

                for (ErrorCompilacion error : errores) {
                    sb.append("- ")
                            .append(error.getTipo())
                            .append(" (línea ").append(error.getLinea() + 1)
                            .append(", columna ").append(error.getColumna() + 1)
                            .append("): ")
                            .append(error.getMensaje())
                            .append("\n");
                }

                return sb.toString();
            }

        } catch (Exception e) {
            return "Error durante la compilación: " + e.getMessage();
        }
    }

    /**
     * Analiza el código fuente y devuelve una lista de errores encontrados.
     * @param codigo El código fuente a analizar
     * @return Lista de errores de compilación
     */
    public List<ErrorCompilacion> analizarCodigo(String codigo) {
        List<ErrorCompilacion> errores = new ArrayList<>();

        try {
            // Crear primero el parser con un lexer temporal/nulo
            Parser parser = new Parser(null);
            parser.errores = errores;

            // Luego crear el lexer con el parser
            Lexer lexer = new Lexer(new StringReader(codigo), parser);

            // Actualizar el scanner del parser con el lexer creado
            parser.setScanner(lexer);

            // Ejecutar el análisis sintáctico
            parser.parse();

            // Agregar errores semánticos de la tabla de símbolos
            errores.addAll(parser.symbolTable.getErrors());

        } catch (Exception e) {
            // Capturar cualquier error no controlado durante el análisis
            errores.add(new ErrorCompilacion("Desconocido", 0, 0,
                    "Error durante el análisis: " + e.getMessage()));
            e.printStackTrace();
        }

        return errores;
    }
}