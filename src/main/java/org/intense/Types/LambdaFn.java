package org.intense.Types;

import org.intense.Env;
import org.intense.ast.ASTNode;

import java.util.ArrayList;
import java.util.List;

public class LambdaFn extends FnVal {
    private final List<String> params;
    private final List<ASTNode> body;
    private final Env closure;

    public LambdaFn(List<String> params, List<ASTNode> body, Env closure) {
        this.params = params;
        this.body = body;
        this.closure = closure;
    }

    @Override
    public int getParamCount() {
        return params.size();
    }

    @Override
    public Value apply(List<Value> args, Env callerEnv) {

            if (args.size() != params.size()) {
                throw new RuntimeException("Expected " + params.size() + " args, got " + args.size());
            }
            // new local env chained to closure
            Env localEnv = new Env(closure);
            for (int i = 0; i < params.size(); i++) {
                localEnv.define(params.get(i), args.get(i));
            }
            // Evaluate body in this new environment
            Value result = null;
            for (ASTNode stmt : body)
            {
                result = stmt.eval(localEnv);
            }
            return result;

    }
}

