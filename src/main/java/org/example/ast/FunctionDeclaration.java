package org.example.ast;

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

    // Getters
    public String getName() {
        return name;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public List<Statement> getBody() {
        return body;
    }
}