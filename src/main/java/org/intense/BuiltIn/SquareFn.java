package org.intense.BuiltIn;

import org.intense.Env;
import org.intense.Types.BuiltinFnVal;
import org.intense.Types.NumVal;
import org.intense.Types.Value;

class SquareFn extends BuiltinFnVal {
    @Override
    public int getParamCount() {
        return 1;
    }

    @Override
    public Value apply(Value[] args, Env env) {
        if (args.length != 1) {
            throw new RuntimeException("square expects 1 argument, got " + args.length);
        }
        double x = args[0].asNumber();
        return new NumVal((x * x));
    }
}

