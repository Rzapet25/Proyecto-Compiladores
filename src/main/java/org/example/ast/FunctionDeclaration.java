package org.example.ast;
import org.example.tac.TACGenerator;

import java.util.List;


public class FunctionDeclaration extends Statement {
    private String name;
    private List<String> parameters;
    private List<Statement> body;

    public FunctionDeclaration(String name, List<String> parameters, List<Statement> body) {
        this.name = name;
        this.parameters = parameters;
        this.body = body;
    }

    @Override

   public String generateTAC(TACGenerator generator) {
       StringBuilder code = new StringBuilder();

       // Registrar la función en el generador TAC
       generator.addFunction(name);

       // Generar etiqueta de función
       code.append("label ").append(name).append(":\n");

       // Generar asignación de parámetros
       for (String param : parameters) {
           code.append("param ").append(param).append("\n");
       }

       // Generar cuerpo de función
       for (Statement stmt : body) {
           code.append(stmt.generateTAC(generator));
       }

       // Almacenar el código completo de la función en el generador
       generator.addFunctionBody(name, code.toString());

       // Devolvemos cadena vacía porque el código se ha añadido al generador
       return "";
   }
//    public String generateTAC(TACGenerator generator) {
//        StringBuilder code = new StringBuilder();
//
//        // Generate function label
//        code.append("label ").append(name).append(":\n");
//
//        // Generate parameter assignment
//        for (String param : parameters) {
//            code.append("param ").append(param).append("\n");
//        }
//
//        // Generate function body
//        for (Statement stmt : body) {
//            code.append(stmt.generateTAC(generator));
//        }
//
//        return code.toString();
//    }
}