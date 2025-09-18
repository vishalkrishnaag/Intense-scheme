package org.intense.ast;

import org.intense.Env;
import org.intense.Types.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CallNode extends ASTNode {
    private ASTNode operand;
    private List<ASTNode> params;

    public CallNode(ASTNode operand, List<ASTNode> params) {
        this.operand = operand;
        this.params = params;
    }

    @Override
    public Value eval(Env env) {

        if(operand instanceof AtomNode atom)
        {
            // Lookup function in environment
            Value lookedUp = env.lookup(atom.getValue().asString());

            // Handle built-in functions
            if (lookedUp instanceof BuiltIn builtin) {
                return builtin.apply(evalBuiltInParams(env));
            }
            if (lookedUp instanceof FnVal fn) {
                Value[] evaluatedArgs = evalParams(env);
                return fn.apply(evaluatedArgs,env);
            }
            return env.lookup(atom.getValue().asString());
        }
        else {
            throw new RuntimeException("function name expected");
        }
    }

    private Value[] evalParams(Env env) {
        if (params == null) return new Value[]{};
        return params.stream().map(p -> p.eval(env)).toArray(Value[]::new);
    }

    private List<Value> evalBuiltInParams(Env env) {
        if (params == null) return Collections.emptyList();
        List<Value> results = new ArrayList<>(params.size());
        for (ASTNode p : params) {
            results.add(p.eval(env));
        }
        return results;
    }

}
