package org.intense.Types;

import org.intense.Env;

import java.util.List;

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

    public abstract Value apply(List<Value> args, Env env);
}
