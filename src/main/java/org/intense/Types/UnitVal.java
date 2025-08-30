package org.intense.Types;

public class UnitVal extends Value {
    public static final UnitVal INSTANCE = new UnitVal();

    public UnitVal() {
    }

    @Override
    public String toString() {
        return "unit";
    }
}
