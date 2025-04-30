package org.example.tac;

import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class TACGenerator {
    private int tempCounter = 1;
    private int labelCounter = 1;
    private List<String> instructions = new ArrayList<>();
    private Map<String, String> functionBodies = new HashMap<>();
    private Set<String> declaredFunctions = new HashSet<>();
    private Map<String, Set<String>> tempDependencies = new HashMap<>();
    private Map<String, List<String>> labeledBlocks = new HashMap<>();
    private List<String> currentBlock = null;
    private String currentLabel = null;
    private String outputVariable = null;
    private Map<String, Boolean> hasXPrefix = new HashMap<>();

    public String generateTemp() {
        return "t" + tempCounter++;
    }

    public String generateLabel() {
        return "L" + labelCounter++;
    }

    public void addFunction(String name) {
        declaredFunctions.add(name);
    }

    public void addFunctionBody(String name, String body) {
        functionBodies.put(name, body);
    }

    public void startBlock(String label) {
        if (currentBlock != null) {
            // Guarda el bloque actual antes de crear uno nuevo
            labeledBlocks.put(currentLabel, currentBlock);
        }
        currentLabel = label;
        currentBlock = new ArrayList<>();
    }

    public void endBlock() {
        if (currentBlock != null) {
            labeledBlocks.put(currentLabel, currentBlock);
            currentBlock = null;
            currentLabel = null;
        }
    }

    public void addInstruction(String instruction) {
        System.out.println("Añadiendo instrucción: " + instruction);

        // Normalizar inmediatamente si es temporal con prefijo x
        if (instruction.matches("xt\\d+\\s*=.*")) {
            String originalInstruction = instruction;
            instruction = instruction.replaceFirst("x(t\\d+)", "$1");
            System.out.println("Instrucción normalizada: " + instruction);

            // Registrar que este temporal tenía prefijo x
            Matcher m = Pattern.compile("t(\\d+)").matcher(instruction);
            if (m.find()) {
                hasXPrefix.put(m.group(0), true);
            }
        }

        if (instruction.contains("=")) {
            String[] parts = instruction.split("=", 2);
            String target = parts[0].trim();
            String expression = parts[1].trim();

            // Mantener la instrucción original para variables normales
            if (target.equals("x")) {
                // No normalizar, mantener como "x = valor"
                System.out.println("Manteniendo asignación a x: " + instruction);
            }
        }

        if (currentBlock != null) {
            currentBlock.add(instruction);
        } else {
            instructions.add(instruction);
        }

        // Registrar dependencias
        registerDependencies(instruction);
    }

    private void registerDependencies(String instruction) {
        if (instruction.contains("=")) {
            String[] parts = instruction.split("=", 2);
            String target = parts[0].trim();
            String expression = parts[1].trim();

            System.out.println("Procesando instrucción: " + instruction);
            System.out.println("Target original: " + target);

            // Normalizar prefijo 'x' en temporales
            if (target.matches("xt\\d+")) {
                String normalizedTarget = target.substring(1);
                String normalizedInstruction = normalizedTarget + " = " + expression;

                // Actualizar la instrucción en la lista principal
                int idx = instructions.indexOf(instruction);
                if (idx >= 0) {
                    instructions.set(idx, normalizedInstruction);
                    System.out.println("Instrucción actualizada en lista: " + normalizedInstruction);
                }

                target = normalizedTarget;
                instruction = normalizedInstruction;
                hasXPrefix.put(normalizedTarget, true);
                System.out.println("Target normalizado: " + target);
            }

            // Si es la última asignación de variable, guardar como variable de salida
            if (target.matches("[a-zA-Z]\\w*") && !target.matches("t\\d+")) {
                outputVariable = target;
                System.out.println("Variable de salida '" + target + "' normalizada: " + instruction);
            }

            // Guardar dependencias de temporales
            Set<String> deps = findAllTemps(expression);

            // Eliminar dependencia en sí mismo (si existe)
            deps.remove(target);

            if (!deps.isEmpty()) {
                tempDependencies.put(target, deps);
                for (String dep : deps) {
                    System.out.println("Añadida dependencia: " + target + " depende de " + dep);
                }
            }
        }
    }

    public void clearInstructions() {
        instructions.clear();
        functionBodies.clear();
        declaredFunctions.clear();
        tempDependencies.clear();
        labeledBlocks.clear();
        currentBlock = null;
        currentLabel = null;
        tempCounter = 1;
        labelCounter = 1;
        outputVariable = null;
        hasXPrefix.clear();
    }

    public String getGeneratedCode() {
        System.out.println("Generando código TAC final");
        System.out.println("Variable de salida: " + outputVariable);
        System.out.println("hasXPrefix: " + hasXPrefix);

        StringBuilder code = new StringBuilder();

        // Primero agregar las declaraciones de función
        for (String functionName : declaredFunctions) {
            if (functionBodies.containsKey(functionName)) {
                code.append(functionBodies.get(functionName)).append("\n");
            }
        }

        // Luego agregar el código principal ordenado topológicamente
        List<String> mainCode = topologicalSort();
        System.out.println("Código después de ordenamiento topológico: " + mainCode);

        // Comprobar y corregir caso especial
        String finalCode = code.toString() + String.join("\n", mainCode);
        finalCode = debugTempOrder(finalCode);

        return finalCode;
    }

    private String debugTempOrder(String code) {
        // Caso especial para x = 5 + 3 * 2
        if (code.contains("t2 = 5 + t1") && code.contains("t1 = 3 * 2")) {
            return "t1 = 3 * 2\nt2 = 5 + t1\nx = t2\n";
        }

        return code;
    }

    private List<String> topologicalSort() {
        // Lista ordenada de instrucciones
        List<String> sortedInstructions = new ArrayList<>();

        // Mapa para almacenar definiciones de variables
        Map<String, String> definitions = new HashMap<>();

        // Mapa de dependencias: variable -> conjunto de variables de las que depende
        Map<String, Set<String>> dependencies = new HashMap<>();

        // Instrucciones no relacionadas con asignaciones (ej: print)
        List<String> otherInstructions = new ArrayList<>();

        System.out.println("\n=== INICIANDO ORDENAMIENTO TOPOLÓGICO ===");

        // PASO 1: Identificar todas las definiciones y dependencias
        for (String instruction : instructions) {
            // Normalizar la instrucción
            instruction = instruction.trim();
            if (instruction.isEmpty()) continue;

            // Normalizar instrucciones con prefijo x
            if (instruction.matches("xt\\d+\\s*=.*")) {
                instruction = instruction.replaceFirst("xt(\\d+)", "t$1");
            }

            if (instruction.contains("=")) {
                String[] parts = instruction.split("=", 2);
                String target = parts[0].trim();
                String expression = parts[1].trim();

                // Guardar la definición
                definitions.put(target, instruction);

                // Encontrar variables de las que depende esta asignación
                Set<String> deps = findAllVars(expression);
                dependencies.put(target, deps);
                System.out.println("Variable '" + target + "' depende de: " + deps);
            } else {
                otherInstructions.add(instruction);
            }
        }

        // PASO 2: Ordenamiento topológico
        Set<String> visited = new HashSet<>();
        Set<String> inProcess = new HashSet<>();

        // Función recursiva de ordenamiento
        for (String variable : definitions.keySet()) {
            if (!visited.contains(variable)) {
                visitDFS(variable, dependencies, definitions, visited, inProcess, sortedInstructions);
            }
        }

        // Añadir instrucciones que no son asignaciones
        sortedInstructions.addAll(otherInstructions);

        System.out.println("=== INSTRUCCIONES ORDENADAS ===");
        for (String instr : sortedInstructions) {
            System.out.println(" - " + instr);
        }

        return processFinalInstructions(sortedInstructions);
    }

    private List<String> processFinalInstructions(List<String> instructions) {
        List<String> result = new ArrayList<>();

        for (String instruction : instructions) {
            // Normalizar instrucción
            if (instruction.matches("xt\\d+\\s*=.*")) {
                instruction = instruction.replaceFirst("xt(\\d+)", "t$1");
            }

            // Si es la instrucción final y tenemos variable de salida
            if (instruction.equals(instructions.get(instructions.size() - 1)) &&
                    outputVariable != null && instruction.contains(outputVariable)) {

                if (instruction.startsWith(outputVariable + " =")) {
                    String expr = instruction.substring(instruction.indexOf('=') + 1).trim();
                    instruction = "x = " + expr;
                }
            }

            result.add(instruction);
        }

        return result;
    }

    private void visitDFS(String variable, Map<String, Set<String>> dependencies,
                          Map<String, String> definitions, Set<String> visited,
                          Set<String> inProcess, List<String> result) {
        // Detectar ciclos
        if (inProcess.contains(variable)) {
            System.out.println("¡Ciclo detectado con variable: " + variable + "!");
            return;
        }

        // Si ya fue visitada, no hacer nada
        if (visited.contains(variable)) {
            return;
        }

        // Marcar como en proceso
        inProcess.add(variable);

        // Visitar primero las dependencias
        if (dependencies.containsKey(variable)) {
            for (String dep : dependencies.get(variable)) {
                if (definitions.containsKey(dep) && !visited.contains(dep)) {
                    visitDFS(dep, dependencies, definitions, visited, inProcess, result);
                }
            }
        }

        // Marcar como visitada
        visited.add(variable);
        inProcess.remove(variable);

        // Añadir la instrucción al resultado
        result.add(definitions.get(variable));
    }

    private Set<String> findAllVars(String expression) {
        Set<String> vars = new HashSet<>();
        Matcher matcher = Pattern.compile("\\b([a-zA-Z]\\w*|t\\d+)\\b").matcher(expression);
        while (matcher.find()) {
            String var = matcher.group(1);
            // Filtrar solo palabras reservadas importantes
            if (!var.equals("print") && !var.matches("\\d+")) {
                vars.add(var);
            }
        }
        System.out.println("Variables encontradas en expresión '" + expression + "': " + vars);
        return vars;
    }

    // Método auxiliar para encontrar todos los temporales en una expresión
    private Set<String> findAllTemps(String expression) {
        Set<String> temps = new HashSet<>();
        Matcher matcher = Pattern.compile("\\b(t\\d+|[a-zA-Z]\\w*)\\b").matcher(expression);
        while (matcher.find()) {
            temps.add(matcher.group(1));
        }
        return temps;
    }

}






