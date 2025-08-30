package org.intense.ast;

import org.intense.Env;
import org.intense.Types.FnVal;
import org.intense.Types.LambdaFn;
import org.intense.Types.StrVal;
import org.intense.Types.Value;

import java.util.List;

public class DefNode extends ASTNode {
    private final AtomNode name;
    private final ASTNode returnType;    // not used yet
    private final List<String> arguments; // null or empty = variable definition
    private final List<ASTNode> body;    // for function, list of statements
    private final ASTNode valueExpr;     // for variable definition

    // Constructor for function definition
    public DefNode(AtomNode name, ASTNode returnType,
                   List<String> arguments, List<ASTNode> body) {
        this.name = name;
        this.returnType = returnType;
        this.arguments = arguments;
        this.body = body;
        this.valueExpr = null;
    }

    // Constructor for variable definition
    public DefNode(AtomNode name, ASTNode valueExpr) {
        this.name = name;
        this.returnType = null;
        this.arguments = null;
        this.body = null;
        this.valueExpr = valueExpr;
    }

    @Override
    public Value eval(Env env) {
        // Evaluate function definition
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

            Value fnName = name.eval(env);
            if (fnName instanceof StrVal strVal) {
                env.define(strVal.value, fn);
            } else {
                throw new RuntimeException("Function name must be identifier");
            }

            return new StrVal("<defined function " + fnName + ">");
        }

        // Evaluate variable definition
        else if (valueExpr != null) {
            Value val = valueExpr.eval(env);

            Value varName = name.eval(env);
            if (varName instanceof StrVal strVal) {
                env.define(strVal.value, val);
            } else {
                throw new RuntimeException("Variable name must be identifier");
            }

            return val;
        }

        throw new RuntimeException("Invalid definition (neither function nor variable)");
    }
}

