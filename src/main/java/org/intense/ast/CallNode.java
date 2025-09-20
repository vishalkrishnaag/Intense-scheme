package org.intense.ast;

import org.intense.Env;
import org.intense.Types.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

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
            Set<String> protectedSymbols = Set.of("make-instance!","make-copy!");
            // Lookup function in environment
            Value lookedUp = env.lookup(atom.getValue().asString());
            if (lookedUp instanceof ReferenceValue reference) {
                lookedUp = env.lookup(reference.getParent());
            }

            if(protectedSymbols.contains(atom.getValue().asString()))
            {
                if (lookedUp instanceof BuiltIn builtin) {
                    return builtin.apply(solveBuiltInParams());
                }
            }

            // Handle built-in functions
            if (lookedUp instanceof BuiltIn builtin) {
                return builtin.apply(evalBuiltInParams(env));
            }
            if (lookedUp instanceof FnVal fn) {
                Value[] evaluatedArgs = evalParams(env);
                return fn.apply(evaluatedArgs,env);
            }

            if (lookedUp instanceof PairFn pair) {
                return pair.getSecond().apply(evalWithLeftParams(env,pair.getFirst()));
            }


            return lookedUp;
        }
        else {
            throw new RuntimeException("function name expected");
        }
    }

    private Value[] evalParams(Env env) {
        if (params == null) return new Value[]{};
        return params.stream().map(p -> p.eval(env)).toArray(Value[]::new);
    }

    private ArrayList<Value> evalWithLeftParams(Env env, Value left) {
        ArrayList<Value> evaluatedParams = new ArrayList<>();
        evaluatedParams.add(left);
        if (params != null && !params.isEmpty()) {
            for (ASTNode p : params) {
                evaluatedParams.add(p.eval(env));
            }
        }
        return evaluatedParams;
    }


    private ArrayList<Value> solveBuiltInParams() {
        ArrayList<Value> evaluatedParams = new ArrayList<>();
        if (params != null && !params.isEmpty()) {
            for (ASTNode p : params) {
                if(p instanceof AtomNode atomic)
                {
                    evaluatedParams.add(atomic.getValue());
                }
                else {
                    throw new RuntimeException("params should be variable");
                }

            }
        }
        return evaluatedParams;
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
