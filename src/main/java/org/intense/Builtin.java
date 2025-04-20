package org.intense;

import org.intense.Ast.ASTNode;
import org.intense.Ast.AtomNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Builtin {

    static int size =0;
    static AtomNode atom;
    static ASTNode first;
    static List<ASTNode> elements;
    static String operation;

    public Builtin(String mOperation,int mSize,ASTNode mFirst,AtomNode mAtom, List<ASTNode> body) {
        size = mSize;
        atom = mAtom;
        first = mFirst;
        elements = body;
        operation = mOperation;
    }



    @FunctionalInterface
    interface BuiltInFunction {
        Object apply();
    }

    private static final Map<String, BuiltInFunction> builtInMethods = new HashMap<>();
    private static Object arithmetic_helper(){
        if (size < 3) throw new IllegalArgumentException(operation + " expects at least 2 operands");
        Object firstEval = elements.get(1).eval();
        if (!(firstEval instanceof Number)) throw new IllegalArgumentException("Non-numeric operand");

        double result = ((Number) firstEval).doubleValue();
        for (int i = 2; i < size; i++) {
            Object nextEval = elements.get(i).eval();
            if (!(nextEval instanceof Number)) throw new IllegalArgumentException("Non-numeric operand");
            double next = ((Number) nextEval).doubleValue();

            switch (operation) {
                case "+" -> result += next;
                case "-" -> result -= next;
                case "*" -> result *= next;
                case "/" -> {
                    if (next == 0) throw new ArithmeticException("Division by zero");
                    result /= next;
                }
            }
        }
        return result;
    }

    static {
        builtInMethods.put("if",() -> {
            if (size != 4) throw new IllegalArgumentException("Invalid if expression, expected 3 operands");
            Object condition = elements.get(1).eval();
            boolean result = condition != null && condition != (Boolean) false;
            return  (result
                    ? elements.get(2).eval()
                    : elements.get(3).eval());
        });

        builtInMethods.put("begin",() -> {
            Object result = null;
            for (int i = 1; i < size; i++) {
                result = elements.get(i).eval();
            }
            return result;
        });

        builtInMethods.put("display",() -> {
            if (size < 2) return new RuntimeException("display expects exactly 1 argument");
            Object val = elements.get(1).eval();
            System.out.println(val);
            return  "()";
        });

        builtInMethods.put("+", Builtin::arithmetic_helper);
        builtInMethods.put("-", Builtin::arithmetic_helper);
        builtInMethods.put("*", Builtin::arithmetic_helper);
        builtInMethods.put("/", Builtin::arithmetic_helper);
        builtInMethods.put("%", Builtin::arithmetic_helper);
        builtInMethods.put("^", Builtin::arithmetic_helper);
        builtInMethods.put("and", Builtin::arithmetic_helper);
        builtInMethods.put("or", Builtin::arithmetic_helper);
        builtInMethods.put("not", Builtin::arithmetic_helper);
        builtInMethods.put("in", Builtin::arithmetic_helper);

    }

    public static Object call() {
        BuiltInFunction function = builtInMethods.get(operation);
        if (function == null) {
            return null;
        }
        return function.apply();
    }
}
