package org.intense.ast;

import org.intense.Env;
import org.intense.Types.*;

import java.util.List;
import java.util.Set;

public class DefNode extends ASTNode {
    private final AtomNode name;
    private LambdaNode lambdaNode;
    private ASTNode valueExpr;


    // Constructor for function definition
    public DefNode(AtomNode name, LambdaNode lambdaNode) {
        Set<String> protectedSymbols = Set.of("+", "-", "*", "/", "%", "<", ">", "!=", "define", "equals?");
        if (protectedSymbols.contains(name.getValue().asString())) {
            throw new RuntimeException("Cannot redefine built-in: " + name.getValue().asString());
        }
        this.name = name;
        this.lambdaNode = lambdaNode;
    }

    // Constructor for variable declaration
    public DefNode(AtomNode name, ASTNode expr) {
        Set<String> protectedSymbols = Set.of("+", "-", "*", "/", "%", "<", ">", "!=", "define", "equals?");
        if (protectedSymbols.contains(name.getValue().asString())) {
            throw new RuntimeException("Cannot redefine built-in: " + name.getValue().asString());
        }
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
                String valueName = idVal.value;
                int dotIndex = valueName.indexOf(".");
                String root = dotIndex == -1 ? valueName : valueName.substring(0, dotIndex);
                String remainder = dotIndex == -1 ? "" : valueName.substring(dotIndex);

                if (dotIndex != -1) {
                    Value type = env.lookup(root);
                    if (type instanceof ReferenceValue ref) {
                        // replace alias with parent name
                        root = ref.getParent();
                        valueName = root + remainder;
                        env.define(valueName, val);
                    } else {
                        env.define(idVal.value, val);
                    }
                } else {
                    env.define(idVal.value, val);
                }
            } else {
                throw new RuntimeException("Variable name must be identifier");
            }

            return val;
        } else {
            Value function = lambdaNode.eval(env);
            env.define(name.getValue().asString(), function);
            return new StrVal("#<Lambda " + name.toString() + ">");
        }
    }
}

