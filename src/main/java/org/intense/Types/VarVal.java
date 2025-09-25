package org.intense.Types;

//for storing Variables
public class VarVal extends Value {
    public final String value;
    public boolean chained = false;

    public VarVal(String value,Boolean isChainEnabled) {
        this.chained = isChainEnabled;
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
