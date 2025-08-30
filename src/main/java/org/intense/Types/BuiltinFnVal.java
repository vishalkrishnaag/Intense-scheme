package org.intense.Types;

import org.intense.Env;

public abstract class BuiltinFnVal extends Value{

    public int getParamCount() {
        return 0;
    }

    public abstract Value apply(Value[] args, Env env);
}
