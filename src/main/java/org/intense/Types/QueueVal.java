package org.intense.Types;

import java.util.Queue;
import java.util.Stack;

public class QueueVal extends Value {
    public Queue<Value> value;

    public QueueVal(Queue<Value> value) {
        this.value = value;
    }
    @Override public String asString() { return value.toString(); }

    @Override
    public String toString() {
        return value.toString();
    }
}
