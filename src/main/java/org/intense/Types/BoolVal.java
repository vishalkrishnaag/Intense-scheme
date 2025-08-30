package org.intense.Types;

public class BoolVal extends Value {
    public final boolean value;

    public BoolVal(boolean value) {
        this.value = value;
    }
    @Override public boolean asBoolean() { return value; }

    @Override
    public String toString() {
        return Boolean.toString(value);
    }
}
