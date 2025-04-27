package org.intense;

import org.intense.Ast.ASTNode;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Environment {

    private final Map<String,ASTNode> localstore = new ConcurrentHashMap<>();
    private final Map<String,ASTNode> Store = new ConcurrentHashMap<>();
    private  Environment parent;

    public Environment(Environment env) {
        parent = env;
    }

    public void define(String name,ASTNode value) {
        localstore.put(name, value);
    }

    public ASTNode lookup(String name) {
        if(name.contains("."))
        {
            if (Store.containsKey(name)) return Store.get(name);
            if (parent != null) return parent.lookup(name);
            throw new RuntimeException("Unbound variable: " + name);
        }

        if (localstore.containsKey(name)) return localstore.get(name);
        if (parent != null) return parent.lookup(name);
        throw new RuntimeException("Unbound variable: " + name);
    }

    public void set(String name,ASTNode value) {
        if (localstore.containsKey(name)) {
            localstore.put(name, value);
        } else if (parent != null) {
            parent.set(name, value);
        } else {
            throw new RuntimeException("Unbound variable: " + name);
        }
    }

    public void delete(String name) {
        localstore.remove(name);
    }

    public void clear() {
        localstore.clear();
    }
}

