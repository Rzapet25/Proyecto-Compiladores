package org.example;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import org.example.error.ErrorCompilacion;
import org.example.lexer.Lexer;
import org.example.parser.Parser;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase principal para analizar un programa escrito en SimpleScript.
 * El programa realiza análisis léxico y sintáctico, sin generar código intermedio.
 */
public class Main {
    /**
     * Método principal que inicia el análisis del programa.
     *
     * @param args Argumentos de línea de comandos. Se espera un archivo de entrada.
     */
    public static void main(String[] args) {
        String inputFile;

        if (args.length != 1) {
            System.out.println("Usando archivo de entrada predeterminado");
            inputFile = "test-files/prueba.ss";
        } else {
            inputFile = args[0];
        }

        try {
            // Leer el archivo de entrada
            Reader reader = new FileReader(inputFile);

            // Crear el analizador sintáctico
            Parser parser = new Parser(null);

            // Lista para almacenar errores
            List<ErrorCompilacion> errores = parser.errores;

            // Crear el analizador léxico y asociarlo al parser
            Lexer lexer = new Lexer(reader, parser);

            // Establecer el scanner en el parser
            parser.setScanner(lexer);

            // Realizar el análisis
            parser.parse();

            // Verificar si hay errores
            if (errores.isEmpty()) {
                System.out.println("===================================");
                System.out.println("Análisis completado exitosamente.");
                System.out.println("No se encontraron errores léxicos ni sintácticos.");
                System.out.println("===================================");
            } else {
                System.out.println("===================================");
                System.out.println("Se encontraron los siguientes errores:");
                for (ErrorCompilacion error : errores) {
                    System.out.printf("- %s (línea %d, columna %d): %s%n",
                            error.getTipo(),
                            error.getLinea() + 1,
                            error.getColumna() + 1,
                            error.getMensaje());
                }
                System.out.println("===================================");
            }

        } catch (Exception e) {
            System.err.println("===================================");
            System.err.println("Error durante el análisis: " + e.getMessage());
            e.printStackTrace();
            System.err.println("===================================");
        }
    }
}