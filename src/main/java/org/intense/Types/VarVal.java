package org.intense.Types;

//for storing Variables
public class VarVal extends Value {
    public final String value;

    public VarVal(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
