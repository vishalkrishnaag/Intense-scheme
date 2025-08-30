package org.intense.Types;

import org.intense.Env;

public abstract class FnVal extends Value{
    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public String functionName;
    public int getParamCount() {
        return 0;
    }

    public Value invoke(Value[] args, Env env) {
        return null;
    }

    public abstract Value apply(Value[] args, Env env);
}
