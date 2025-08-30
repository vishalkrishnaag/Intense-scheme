package org.intense.BuiltIn;

import org.intense.Env;
import org.intense.Types.BuiltinFnVal;
import org.intense.Types.StrVal;
import org.intense.Types.Value;

class PrintFn extends BuiltinFnVal {
    @Override
    public Value apply(Value[] args, Env env) {
        for (Value v : args) {
            System.out.print(v + " ");
        }
        System.out.println();
        return new StrVal("ok");
    }

    @Override
    public String toString() { return "<builtin print>"; }
}
