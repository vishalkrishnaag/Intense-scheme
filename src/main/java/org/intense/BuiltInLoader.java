package org.intense;

import org.intense.Types.*;

import java.util.HashMap;

public class BuiltInLoader {
    public BuiltInLoader(Env env) {
        env.defineBuiltIn("+", new BuiltIn(args -> {
            if (args.isEmpty()) {
                throw new RuntimeException("'+' requires at least one argument");
            }
            double sum = 0;
            for (Value v : args) sum += v.asNumber();
            return new NumVal(sum);
        }));

        env.defineBuiltIn("add?", new BuiltIn(args -> {
            if (args.isEmpty()) {
                throw new RuntimeException("'+' requires at least one argument");
            }
            double sum = 0;
            StringBuilder summ = new StringBuilder();
            for (Value v : args) {
                if (v instanceof StrVal) {
                    summ.append(v.asString());
                } else {
                    if (summ.isEmpty()) {
                        sum += v.asNumber();
                    } else {
                        summ.append(v.asNumber());
                    }

                }
            }
            if (summ.isEmpty()) {
                return new NumVal(sum);
            }
            else {
                return new StrVal(summ.toString());
            }
        }));

        // Subtraction
        env.defineBuiltIn("-", new BuiltIn(args -> {
            if (args.isEmpty()) {
                throw new RuntimeException("'-' requires at least one argument");
            }
            double result = args.get(0).asNumber();
            if (args.size() == 1) {
                // Unary negation
                return new NumVal(-result);
            }
            for (int i = 1; i < args.size(); i++) {
                result -= args.get(i).asNumber();
            }
            return new NumVal(result);
        }));

        // Subtraction
        env.defineBuiltIn("sub?", new BuiltIn(args -> {
            if (args.isEmpty()) {
                throw new RuntimeException("'-' requires at least one argument");
            }
            double result = args.get(0).asNumber();
            if (args.size() == 1) {
                // Unary negation
                return new NumVal(-result);
            }
            for (int i = 1; i < args.size(); i++) {
                result -= args.get(i).asNumber();
            }
            return new NumVal(result);
        }));

        env.defineBuiltIn("make-instance!", new BuiltIn(args -> {
            String parent = (args.get(0)).asString(); // "a"
            String child  = (args.get(1)).asString(); // "p"

            // copy root
            env.define(child, env.lookup(parent));

            // copy dotted children
            for (var entry : env.getSymbol().entrySet()) {
                String key = entry.getKey();
                if (key.startsWith(parent + ".")) {
                    String suffix = key.substring(parent.length()); // ".b" or ".c"
                    env.define(child + suffix, entry.getValue());
                }
            }

            return new StrVal("#<done>");
        }));

        env.defineBuiltIn("make-copy!", new BuiltIn(args -> {
            String parent = (args.get(0)).asString();
            String child  = (args.get(1)).asString();
            env.define(child, new ReferenceValue(parent));
            return new StrVal("#<done>");
        }));


        env.defineBuiltIn("*", new BuiltIn(args -> {
            if (args.isEmpty()) {
                throw new RuntimeException("'*' requires at least one argument");
            }
            double sum = 1;
            for (Value v : args) sum *= v.asNumber();
            return new NumVal(sum);
        }));

        env.defineBuiltIn("mul?", new BuiltIn(args -> {
            if (args.isEmpty()) {
                throw new RuntimeException("'*' requires at least one argument");
            }
            double sum = 1;
            for (Value v : args) sum *= v.asNumber();
            return new NumVal(sum);
        }));

        // Division
        env.defineBuiltIn("/", new BuiltIn(args -> {
            if (args.isEmpty()) {
                throw new RuntimeException("'/' requires at least one argument");
            }
            double result = args.get(0).asNumber();
            if (args.size() == 1) {
                // Reciprocal
                return new NumVal(1 / result);
            }
            for (int i = 1; i < args.size(); i++) {
                result /= args.get(i).asNumber();
            }
            return new NumVal(result);
        }));

        env.defineBuiltIn("div?", new BuiltIn(args -> {
            if (args.isEmpty()) {
                throw new RuntimeException("'/' requires at least one argument");
            }
            double result = args.get(0).asNumber();
            if (args.size() == 1) {
                // Reciprocal
                return new NumVal(1 / result);
            }
            for (int i = 1; i < args.size(); i++) {
                result /= args.get(i).asNumber();
            }
            return new NumVal(result);
        }));


        env.defineBuiltIn("%", new BuiltIn(args -> {
            if (args.size() != 2) {
                throw new RuntimeException("'%' requires exactly 2 arguments");
            }
            double a = args.get(0).asNumber();
            double b = args.get(1).asNumber();
            return new NumVal(a % b);
        }));


        env.defineBuiltIn("not", new BuiltIn(args -> {
            if (args.size() != 1) {
                throw new RuntimeException("'%' requires exactly 1 argument");
            }
            boolean b = ((BoolVal) args.get(0)).asBoolean();
            return new BoolVal(!b);
        }));

        env.defineBuiltIn("=", new BuiltIn(args -> {
            if (args.size() < 2) {
                throw new RuntimeException("'=' requires at least 2 arguments");
            }
            double prev = args.get(0).asNumber();
            for (int i = 1; i < args.size(); i++) {
                double curr = args.get(i).asNumber();
                if (curr != prev) {
                    return new BoolVal(false);
                }
                prev = curr;
            }
            return new BoolVal(true);
        }));

        env.defineBuiltIn("equals?", new BuiltIn(args -> {
            if (args.size() < 2) {
                throw new RuntimeException("'equals?' requires at least 2 arguments");
            }
            double prev = args.get(0).asNumber();
            for (int i = 1; i < args.size(); i++) {
                double curr = args.get(i).asNumber();
                if (curr != prev) {
                    return new BoolVal(false);
                }
                prev = curr;
            }
            return new BoolVal(true);
        }));

        env.defineBuiltIn("<", new BuiltIn(args -> {
            if (args.size() < 2) {
                throw new RuntimeException("'<' requires at least 2 arguments");
            }
            for (int i = 0; i < args.size() - 1; i++) {
                if (!(args.get(i).asNumber() < args.get(i + 1).asNumber())) {
                    return new BoolVal(false);
                }
            }
            return new BoolVal(true);
        }));

        env.defineBuiltIn("less-than?", new BuiltIn(args -> {
            if (args.size() < 2) {
                throw new RuntimeException("'<' requires at least 2 arguments");
            }
            for (int i = 0; i < args.size() - 1; i++) {
                if (!(args.get(i).asNumber() < args.get(i + 1).asNumber())) {
                    return new BoolVal(false);
                }
            }
            return new BoolVal(true);
        }));

        env.defineBuiltIn(">", new BuiltIn(args -> {
            if (args.size() < 2) {
                throw new RuntimeException("'>' requires at least 2 arguments");
            }
            for (int i = 0; i < args.size() - 1; i++) {
                if (!(args.get(i).asNumber() > args.get(i + 1).asNumber())) {
                    return new BoolVal(false);
                }
            }
            return new BoolVal(true);
        }));

        env.defineBuiltIn("<=", new BuiltIn(args -> {
            if (args.size() < 2) {
                throw new RuntimeException("'<=' requires at least 2 arguments");
            }
            for (int i = 0; i < args.size() - 1; i++) {
                if (!(args.get(i).asNumber() <= args.get(i + 1).asNumber())) {
                    return new BoolVal(false);
                }
            }
            return new BoolVal(true);
        }));

        env.defineBuiltIn(">=", new BuiltIn(args -> {
            if (args.size() < 2) {
                throw new RuntimeException("'>=' requires at least 2 arguments");
            }
            for (int i = 0; i < args.size() - 1; i++) {
                if (!(args.get(i).asNumber() >= args.get(i + 1).asNumber())) {
                    return new BoolVal(false);
                }
            }
            return new BoolVal(true);
        }));


        // I/O
        env.defineBuiltIn("display", new BuiltIn(args -> {
            for (Value v : args) {
                System.out.print(v);
            }
            return new BoolVal(true); // convention: return #t
        }));

        env.defineBuiltIn("isNull?", new BuiltIn(args -> {
            if (args.size() != 1) {
                throw new RuntimeException("'isNull?' requires exactly 1 argument");
            }
            if(args.get(0) instanceof StrVal)
            {
                return new BoolVal(true);
            }
           else if(args.get(0) instanceof NumVal)
            {
                return new BoolVal(true);
            }
            else if(args.get(0) instanceof NullVal)
            {
                return new BoolVal(true);
            }
            else if(args.get(0) instanceof VarVal)
            {
                String b = (args.get(0)).asString();
                Value val = env.lookup(b);
                if (val instanceof NullVal)
                {
                    return new BoolVal(true);
                }
                else {
                    return new BoolVal(false);
                }
            }
            else {
                throw new RuntimeException("Invalid operand provided to isNull?");
            }
        }));
    }
}
