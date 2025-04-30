package org.example.ast;

import org.example.tac.TACGenerator;

public interface Node {
    String generateTAC(TACGenerator generator);
}
