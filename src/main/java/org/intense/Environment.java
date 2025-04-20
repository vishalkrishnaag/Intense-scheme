package org.intense;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Optional;

public class Environment {
    private final Map<String, Object> vars = new ConcurrentHashMap<>();
    private final Map<String, Closure> closures = new ConcurrentHashMap<>();
    private final Environment parent;

    public Environment() {
        this.parent = null;
    }

    public Environment(Environment parent) {
        this.parent = parent;
    }

    public void define(String name, Object value) {
        vars.put(name, value);
    }
    public void defineClosure(String name,Closure value) {
        closures.put(name, value);
    }

    public Closure get(String name) {
        Closure value = closures.get(name);
        if (value != null) {
            return value;
        } else if (parent != null) {
            return parent.get(name);
        }
        return null;
    }

    public Optional<Object> lookup(String name) {
        Object value = vars.get(name);
        if (value != null) return Optional.of(value);
        if (parent != null) return parent.lookup(name);
        return Optional.empty();
    }
}

