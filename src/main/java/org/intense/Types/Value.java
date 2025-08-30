package org.intense.Types;

public abstract class Value {
    public double asNumber() { throw new RuntimeException("Not a number"); }
    public boolean asBoolean() { throw new RuntimeException("Not a boolean"); }
    public String asString() { return toString(); }
}
