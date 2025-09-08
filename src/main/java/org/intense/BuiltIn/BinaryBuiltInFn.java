package org.intense.BuiltIn;

import org.intense.Env;
import org.intense.Types.*;

class BinaryBuiltInFn extends BuiltinFnVal {
    @Override
    public Value apply(Value[] args, Env env) {
        for (Value v : args) {
            System.out.print(v + " ");
        }
        System.out.println();
        return new StrVal("ok");
    }

    @Override
    public String toString() { return "<builtin print>"; }


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

    private boolean isOperator(String code) {
        return code.equals("add") || code.equals("sub") || code.equals("mul") ||
                code.equals("div") || code.equals("mod") || code.equals("pow") ||
                code.equals("greater") || code.equals("lesser") || code.equals("not");
    }
}
