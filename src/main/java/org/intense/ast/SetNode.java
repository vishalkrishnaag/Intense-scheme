package org.intense.ast;

import org.intense.Env;
import org.intense.ProtectBuiltIn;
import org.intense.Types.StrVal;
import org.intense.Types.Value;
import org.intense.Types.VarVal;

public class SetNode extends ASTNode {
    private final AtomNode name;
    private LambdaNode lambdaNode;
    private ASTNode valueExpr;


    // Constructor for function definition
    public SetNode(AtomNode name, LambdaNode lambdaNode) {
        new ProtectBuiltIn(name.getValue().asString());
        this.name = name;
        this.lambdaNode = lambdaNode;
    }

    // Constructor for variable declaration
    public SetNode(AtomNode name, ASTNode expr) {
        new ProtectBuiltIn(name.getValue().asString());
        this.name = name;
        this.valueExpr = expr;
    }

    @Override
    public Value eval(Env env) {
        // Evaluate variable definition
        if (valueExpr != null) {
            Value val = valueExpr.eval(env);

            Value varName = name.getValue();
            if (varName instanceof VarVal idVal) {
                env.set(idVal.value, val);
            }
            else {
                throw new RuntimeException("Variable name must be identifier");
            }

            return val;
        } else {
            Value function = lambdaNode.eval(env);
            env.set(name.getValue().asString(), function);
            return new StrVal("#<Lambda " + name + ">");
        }
    }
}