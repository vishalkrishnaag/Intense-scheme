package org.intense.Types;

import org.intense.Env;
import org.intense.ast.ASTNode;

import java.util.List;

public class LambdaFn extends FnVal {
    private final List<String> params;
    private final ASTNode body;
    private final Env closure;

    public LambdaFn(List<String> params, ASTNode body, Env closure) {
        this.params = params;
        this.body = body;
        this.closure = closure;
    }

    @Override
    public int getParamCount() {
        return params.size();
    }

    @Override
    public Value apply(Value[] args, Env callerEnv) {
        if (params != null)
        {
            if (args.length != params.size()) {
                throw new RuntimeException("Expected " + params.size() + " args, got " + args.length);
            }
            // new local env chained to closure
            Env localEnv = new Env(closure);
            for (int i = 0; i < params.size(); i++) {
                localEnv.define(params.get(i), args[i]);
            }
            // Evaluate body in this new environment
            return body.eval(localEnv);
        }
        else
        {
            // Evaluate body in this new environment
            return body.eval(callerEnv);
        }


    }
}

