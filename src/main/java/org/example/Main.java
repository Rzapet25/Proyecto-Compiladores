package org.example;

import java_cup.runtime.Symbol;
import org.example.ast.Program;
import org.example.tac.TACGenerator;
import org.example.lexer.Lexer;
import org.example.parser.Parser;
import org.example.parser.sym;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;


/**
 * Clase principal para compilar un programa escrito en un lenguaje ficticio.
 * El programa se analiza y genera código de tres direcciones (TAC).
 */
public class Main {
    /**
     * Método principal que inicia la compilación del programa.
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

            // Crear analizador léxico
            Lexer lexer = new Lexer(reader);

            // Crear analizador sintáctico
            Parser parser = new Parser(lexer);

            // Analizar el programa
            Symbol result = parser.parse();
            Program program = (Program) result.value;

            String outputFile = inputFile.replaceFirst("[.][^.]+$", "") + ".tac";

            // Generar código de tres direcciones
            TACGenerator generator = new TACGenerator();
            String tacCode = program.generateTAC(generator);
            tacCode += generator.getGeneratedCode();

            System.out.println("=== CÓDIGO TAC ORIGINAL (antes de procesar) ===");
            System.out.println(tacCode);

            // Simplificar el procesamiento para preservar etiquetas y estructura de control
            List<String> instructions = new ArrayList<>();
            for (String line : tacCode.split("\n")) {
                line = line.trim();
                if (line.isEmpty()) continue;

                // Normalizar instrucciones con prefijo 'x' en temporales (mantiene sintaxis)
                if (line.matches("xt\\d+\\s*=.*")) {
                    line = line.replaceFirst("x(t\\d+)", "$1");
                }

                // Corregir casos donde falta la variable objetivo
                if (line.startsWith("= ")) {
                    line = "x" + line;
                }

                instructions.add(line);
            }

            // Conservar la estructura completa del TAC
            StringBuilder finalCode = new StringBuilder();
            for (String instr : instructions) {
                finalCode.append(instr).append("\n");
            }
            // logs para ver el código TAC generado
            System.out.println("=== CÓDIGO TAC FINAL ===");
            System.out.println(finalCode.toString());

            // Escribir el código TAC en el archivo de salida
            try (FileWriter writer = new FileWriter(outputFile)) {
                writer.write(finalCode.toString());
            } catch (IOException e) {
                System.err.println("Error al escribir el archivo de salida: " + e.getMessage());
            }

            System.out.println("Compilación exitosa. Código generado en: " + outputFile);
            System.out.println("\nCódigo TAC generado:");
            System.out.println("---------------------");
            System.out.println(finalCode.toString());

        } catch (Exception e) {
            System.err.println("Error durante la compilación: " + e.getMessage());
            e.printStackTrace();
        }
    }
}