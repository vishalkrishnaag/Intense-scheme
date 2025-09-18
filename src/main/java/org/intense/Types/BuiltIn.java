package org.intense.Types;

import java.util.List;
import java.util.function.Function;

public class BuiltIn extends Value {
    private final Function<List<Value>, Value> impl;

    public BuiltIn(Function<List<Value>, Value> impl) {
        this.impl = impl;
    }

    public Value apply(List<Value> args) {
        return impl.apply(args);
    }

    @Override
    public String toString() {
        return "<BuiltIn>";
    }
}
