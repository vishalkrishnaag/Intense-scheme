package org.intense.Types;

public class NullVal extends Value {
    public static final NullVal INSTANCE = new NullVal();

    public NullVal() {
    }

    @Override
    public String toString() {
        return "null";
    }
}
