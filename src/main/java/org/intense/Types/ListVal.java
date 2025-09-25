package org.intense.Types;

import java.util.List;

public class ListVal extends Value {
    public final List<Value> value;

    public ListVal(List<Value> value) {
        this.value = value;
    }
    @Override public String asString() { return value.toString(); }

    @Override
    public String toString() {
        return value.toString();
    }
}
