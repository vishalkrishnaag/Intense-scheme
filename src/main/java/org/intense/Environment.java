package org.intense;

import org.intense.Ast.ASTNode;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Environment {

    private final Map<String, List<ASTNode>> store = new ConcurrentHashMap<>();
    private final Map<String,String> packageList = new ConcurrentHashMap<>();
    private  Environment parent;

    public Environment(Environment env) {
        parent = env;
    }

    public void define(String name,List<ASTNode> value) {
        store.put(name, value);
    }

    public List<ASTNode> lookup(String name) {
        if (store.containsKey(name)) return store.get(name);
        if (parent != null) return parent.lookup(name);
        throw new RuntimeException("Unbound variable: " + name);
    }

    public void set(String name,List<ASTNode> value) {
        if (store.containsKey(name)) {
            store.put(name, value);
        } else if (parent != null) {
            parent.set(name, value);
        } else {
            throw new RuntimeException("Unbound variable: " + name);
        }
    }

    public void delete(String name) {
        store.remove(name);
    }

    public void clear() {
        store.clear();
    }
}

