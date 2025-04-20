package org.intense;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Optional;

public class Environment {
    private final Map<String, Object> vars = new ConcurrentHashMap<>();
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

    public Optional<Closure> get(String name) {
        Object value = vars.get(name);
        if (value instanceof Closure closure) {
            return Optional.of(closure);
        }
        if (parent != null) {
            return parent.get(name);
        }
        return Optional.empty();
    }

    public Optional<Object> lookup(String name) {
        Object value = vars.get(name);
        if (value != null) return Optional.of(value);
        if (parent != null) return parent.lookup(name);
        return Optional.empty();
    }
}

