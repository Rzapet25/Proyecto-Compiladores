package org.example.ast;


import org.example.tac.TACGenerator;

import java.util.List;

public class Program implements Node {
    private List<Statement> statements;

    public Program(List<Statement> statements) {
        this.statements = statements;
    }

    @Override
    public String generateTAC(TACGenerator generator) {
        StringBuilder code = new StringBuilder();
        for (Statement stmt : statements) {
            code.append(stmt.generateTAC(generator));
        }
        return code.toString();
    }
}