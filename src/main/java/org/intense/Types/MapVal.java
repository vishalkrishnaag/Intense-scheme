package org.intense.Types;

import org.intense.ast.ASTNode;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class MapVal extends Value {
    public Map<Value,Value> value;

    public MapVal(Map<Value,Value> value) {
        this.value = value;
    }
    public MapVal() {
        this.value = new HashMap<>();
    }

    public Value get(Value key) {
        return value.get(key);
    }

    public Value remove(Value key) {
        return value.remove(key);
    }

    public boolean containsKey(Value key) {
        return value.containsKey(key);
    }

    public Map<Value, Value> asMap() {
        // Return read-only view to avoid external mutation
        return Map.copyOf(value);
    }

    @Override public String asString() { return value.toString(); }

    @Override
    public String toString() {
        return value.toString();
    }
}
