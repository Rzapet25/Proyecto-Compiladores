package org.example.service;

import java_cup.runtime.Symbol;
import org.example.ast.Program;
import org.example.tac.TACGenerator;
import org.springframework.stereotype.Service;

import java.io.StringReader;

@Service
public class CompiladorService {

    public String compilar(String codigoFuente) {
        try {
            // Usar StringReader en lugar de FileReader
            StringReader reader = new StringReader(codigoFuente);

            // Crear analizador léxico
            Lexer lexer = new Lexer(reader);

            // Crear analizador sintáctico
            Parser parser = new Parser(lexer);

            // Analizar el programa
            Symbol result = parser.parse();
            Program program = (Program) result.value;

            // Generar código de tres direcciones
            TACGenerator generator = new TACGenerator();
            String tacCode = program.generateTAC(generator);
            tacCode += generator.getGeneratedCode();

            return procesarCodigoTAC(tacCode);

        } catch (Exception e) {
            return "Error durante la compilación: " + e.getMessage();
        }
    }

    private String procesarCodigoTAC(String tacCode) {
        // Tu lógica de procesamiento de código TAC aquí
        // Similar a lo que haces en Main.java
        StringBuilder finalCode = new StringBuilder();

        for (String line : tacCode.split("\n")) {
            line = line.trim();
            if (line.isEmpty()) continue;

            if (line.matches("xt\\d+\\s*=.*")) {
                line = line.replaceFirst("x(t\\d+)", "$1");
            }

            if (line.startsWith("= ")) {
                line = "x" + line;
            }

            finalCode.append(line).append("\n");
        }

        return finalCode.toString();
    }
}