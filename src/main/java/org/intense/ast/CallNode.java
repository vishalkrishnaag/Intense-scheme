package org.intense.ast;

import org.intense.Env;
import org.intense.Types.*;

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
        String code = operand != null ? operand.eval(env).toString() : "";
        boolean argumentRequired = true;

        // Lookup function in environment
        Value lookedUp = env.lookup(code);

        if (code.contains(".")) {
            if (lookedUp instanceof FnVal func) {
                if (func.getParamCount() == 0) {
                    argumentRequired = false;
                }
                if (params != null) {
                    if (params.size() != func.getParamCount()) {
                        throw new RuntimeException(
                                "Argument Count Mismatch: method " + code +
                                        " expects " + func.getParamCount() +
                                        " arguments but user provided " + params.size()
                                );
                    }
                }
                if (params == null && argumentRequired) {
                    throw new RuntimeException(
                            "Method " + code + " expects " + func.getParamCount() + " arguments but user provided none"
                            );
                }
            }
        }

        // Handle built-in functions
        if (lookedUp instanceof BuiltinFnVal builtin) {
            Value[] evaluatedArgs = evalParams(env);
            return builtin.apply(evaluatedArgs,env);
        }

        boolean annotated = false;
        if (!code.isEmpty() && code.charAt(0) == '@') {
            if (code.contains("-")) {
                code = "@in10s_" + code.replace("-", "_").replace("@", "");
            }
            annotated = true;
        }

        // Handle arithmetic and logic functions
        if (params != null && !params.isEmpty()) {
            if (isOperator(code)) {
                Value left = params.get(0).eval(env);
                Value right = params.size() > 1 ? params.get(1).eval(env) : null;
                return applyOperator(code, left, right);
            } else {
                // Function call
                Value[] evaluatedArgs = evalParams(env);
                if (lookedUp instanceof FnVal) {
                    FnVal func = (FnVal) lookedUp;
                    return func.invoke(evaluatedArgs, env);
                }
                // otherwise, just return textual representation
                return new StrVal(code + "(" + joinArgs(evaluatedArgs) + ")");
            }
        } else {
            // No params
            if (lookedUp instanceof FnVal) {
                FnVal func = (FnVal) lookedUp;
                return func.invoke(new Value[]{}, env);
            }
            return new StrVal(code + "()");
        }
    }

    private Value[] evalParams(Env env) {
        if (params == null) return new Value[]{};
        return params.stream().map(p -> p.eval(env)).toArray(Value[]::new);
    }

    private boolean isOperator(String code) {
        return code.equals("add") || code.equals("sub") || code.equals("mul") ||
                code.equals("div") || code.equals("mod") || code.equals("pow") ||
                code.equals("greater") || code.equals("lesser") || code.equals("not");
    }

    private Value applyOperator(String code, Value left, Value right) {
        double l = left.asNumber();
        double r = right != null ? right.asNumber() : 0;

        return switch (code) {
            case "add" -> new NumVal(l + r);
            case "sub" -> new NumVal(l - r);
            case "mul" -> new NumVal(l * r);
            case "div" -> new NumVal(l / r);
            case "mod" -> new NumVal(l % r);
            case "pow" -> new NumVal(Math.pow(l, r));
            case "greater" -> new BoolVal(l > r);
            case "lesser" -> new BoolVal(l < r);
            case "not" -> new BoolVal(!(left.asBoolean()));
            default -> throw new RuntimeException("Unknown operator: " + code);
        };
    }

    private String joinArgs(Value[] args) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
        sb.append(args[i].toString());
        if (i < args.length - 1) sb.append(", ");
    }
        return sb.toString();
    }
}
