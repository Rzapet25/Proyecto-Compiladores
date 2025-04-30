package org.example.ast;

import org.example.tac.TACGenerator;

import java.util.List;

public class FunctionCall extends Expression {
    private String name;
    private List<Expression> arguments;

    public FunctionCall(String name, List<Expression> arguments) {
        this.name = name;
        this.arguments = arguments;
    }

    @Override
    public String generateTAC(TACGenerator generator) {
        // Generar código TAC para los argumentos y guardar sus valores temporales
        String[] argTemps = new String[arguments.size()];
        for (int i = 0; i < arguments.size(); i++) {
            argTemps[i] = arguments.get(i).generateTAC(generator);
        }

        // Añadir instrucciones de param con los valores calculados
        for (String argTemp : argTemps) {
            // Añadir directamente al código generado, no al generador
            generator.addInstruction("param " + argTemp);
        }

        // Generar la llamada a función
        String resultTemp = generator.generateTemp();
        generator.addInstruction(resultTemp + " = call " + name);

        return resultTemp;
    }
}