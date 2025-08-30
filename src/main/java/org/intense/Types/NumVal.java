package org.intense.Types;

import org.jetbrains.annotations.NotNull;

public class NumVal extends Value {
    public final double value;

    public NumVal(@NotNull Double value) {
        this.value = value;
    }

    @Override
    public double asNumber() { return value; }

    @Override
    public String toString() {
        return Double.toString(value);
    }
}
