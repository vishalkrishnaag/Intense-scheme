package org.intense.Types;

public class PairFn extends Value{
    private Value first;
    private BuiltIn second;

    public PairFn(Value key, BuiltIn value) {
        this.first = key;
        this.second = value;
    }

    public Value getFirst() {
        return first;
    }

    public void setFirst(Value first) {
        this.first = first;
    }

    public BuiltIn getSecond() {
        return second;
    }

    public void setSecond(BuiltIn second) {
        this.second = second;
    }
}

