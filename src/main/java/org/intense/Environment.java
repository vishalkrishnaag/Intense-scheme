package org.intense;

import org.intense.Ast.ASTNode;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Environment {
    private final Map<String, List<ASTNode>> store = new ConcurrentHashMap<>();
    private final Map<String,TokenType> typeStore = new ConcurrentHashMap<>();
    private final Environment parent;

    public Environment() {
        this.parent = null;
    }

    public Environment(Environment parent) {
        this.parent = parent;
    }

    public void define(String name,List<ASTNode> value) {
        store.put(name, value);
    }

    public List<ASTNode> lookup(String name) {
        List<ASTNode> value = store.get(name);
        if (value != null) return value;
        if (parent != null) return parent.lookup(name);
        return null;
    }

    public void delete(String name) {
        store.remove(name);
    }

    public void clear() {
        store.clear();
    }
}

