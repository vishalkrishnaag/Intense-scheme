package org.intense.Types;

import java.util.List;
import java.util.Stack;

public class StackVal extends Value {
    public  Stack<Value> value;

    public StackVal(Stack<Value> value) {
        this.value = value;
    }
    @Override public String asString() { return value.toString(); }

    @Override
    public String toString() {
        return value.toString();
    }
}
