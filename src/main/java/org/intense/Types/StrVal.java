package org.intense.Types;

public class StrVal extends Value {
    public final String value;

    public StrVal(String value) {
        this.value = value;
    }
    @Override public String asString() { return value; }

    @Override
    public String toString() {
        return value;
    }
}
