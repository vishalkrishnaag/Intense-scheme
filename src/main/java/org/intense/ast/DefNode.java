package org.intense.ast;

import org.intense.Env;
import org.intense.Types.*;

import java.util.List;

public class DefNode extends ASTNode {
    private final AtomNode name;
    private final List<String> arguments;
    private final List<ASTNode> body;
    private final ASTNode valueExpr;
    private final boolean isFunction;


    // Constructor for function definition
    public DefNode(AtomNode name,
                   List<String> arguments, List<ASTNode> body, boolean isFunction) {
        this.name = name;
        this.arguments = arguments;
        this.body = body;
        this.valueExpr = null;
        this.isFunction = isFunction;
    }

    // Constructor for variable declaration
    public DefNode(AtomNode name, ASTNode expr, boolean isFunction) {
        this.name = name;
        this.arguments = null;
        this.body = null;
        this.valueExpr = expr;
        this.isFunction = isFunction;
    }

    @Override
    public Value eval(Env env) {
        // Evaluate function definition
        if (isFunction) {
            if (arguments != null) {
                ASTNode functionBody = new ASTNode() {
                    @Override
                    public Value eval(Env localEnv) {
                        Value last = new StrVal("nil");
                        for (ASTNode stmt : body) {
                            last = stmt.eval(localEnv);
                        }
                        return last;
                    }
                };

                LambdaFn fn = new LambdaFn(arguments, functionBody, env);

                Value fnName = name.getValue();
                if (fnName instanceof VarVal val) {
                    env.define(val.value, fn);
                } else {
                    throw new RuntimeException("Function name must be identifier");
                }

                return new StrVal("<defined function " + fnName + ">");
            }
            else {
                ASTNode functionBody = new ASTNode() {
                    @Override
                    public Value eval(Env localEnv) {
                        Value last = new StrVal("nil");
                        for (ASTNode stmt : body) {
                            last = stmt.eval(localEnv);
                        }
                        return last;
                    }
                };

                LambdaFn fn = new LambdaFn(null, functionBody, env);

                Value fnName = name.getValue();
                if (fnName instanceof VarVal val) {
                    env.define(val.value, fn);
                } else {
                    throw new RuntimeException("Function name must be identifier");
                }
                return new StrVal("<defined function " + fnName + ">");
            }
        } else {
            // Evaluate variable definition
            if (valueExpr != null) {
                Value val = valueExpr.eval(env);

                Value varName = name.getValue();
                if (varName instanceof VarVal idVal) {
                    env.define(idVal.value, val);
                } else {
                    throw new RuntimeException("Variable name must be identifier");
                }

                return val;
            }
        }

        throw new RuntimeException("Invalid definition (neither function nor variable)");
    }
}

