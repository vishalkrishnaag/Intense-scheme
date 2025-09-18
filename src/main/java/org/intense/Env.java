package org.intense;

import org.intense.Types.BoolVal;
import org.intense.Types.BuiltIn;
import org.intense.Types.NumVal;
import org.intense.Types.Value;

import java.util.HashMap;
import java.util.Map;

public class Env {
    private final Env parent;
    private final Map<String, Value> symbols = new HashMap<>();

    public Env() {
        this.parent = null;
    }

    public Env(Env parent) {
        this.parent = parent;
    }

    // Define a new variable in *this* scope
    public void define(String name, Value value) {
        symbols.put(name, value);
    }

    // Lookup variable (searches up the parent chain)
    public Value lookup(String name) {
        if (symbols.containsKey(name)) {
            return symbols.get(name);
        } else if (parent != null) {
            return parent.lookup(name);
        } else {
            throw new RuntimeException("Undefined symbol: " + name);
        }
    }

    // Set existing variable (mutates closest scope where it's defined)
    public void set(String name, Value value) {
        if (symbols.containsKey(name)) {
            symbols.put(name, value);
        } else if (parent != null) {
            parent.set(name, value);
        } else {
            throw new RuntimeException("Undefined symbol: " + name);
        }
    }
}
