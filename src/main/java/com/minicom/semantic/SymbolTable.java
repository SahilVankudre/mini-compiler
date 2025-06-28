package com.minicom.semantic;

import java.util.HashMap;
import java.util.Map;

public class SymbolTable {
    private final Map<String, String> symbols = new HashMap<>();

    public void declare(String name, String type) throws SemanticException {
        if (symbols.containsKey(name)) {
            throw new SemanticException("Variable '" + name + "' already declared.");
        }
        symbols.put(name, type);
    }

    public boolean isDeclared(String name) {
        return symbols.containsKey(name);
    }

    public String getType(String name) {
        return symbols.get(name);
    }
}
