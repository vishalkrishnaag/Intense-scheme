package org.intense;

import org.intense.Types.BoolVal;
import org.intense.Types.BuiltIn;
import org.intense.Types.NumVal;
import org.intense.Types.Value;

public class BuiltInLoader {
    public BuiltInLoader(Env env) {
        env.define("+", new BuiltIn(args -> {
            if (args.isEmpty()) {
                throw new RuntimeException("'+' requires at least one argument");
            }
            double sum = 0;
            for (Value v : args) sum += v.asNumber();
            return new NumVal(sum);
        }));

        env.define("add?", new BuiltIn(args -> {
            if (args.isEmpty()) {
                throw new RuntimeException("'+' requires at least one argument");
            }
            double sum = 0;
            for (Value v : args) sum += v.asNumber();
            return new NumVal(sum);
        }));

        // Subtraction
        env.define("-", new BuiltIn(args -> {
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
        env.define("sub?", new BuiltIn(args -> {
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

        env.define("*", new BuiltIn(args -> {
            if (args.isEmpty()) {
                throw new RuntimeException("'*' requires at least one argument");
            }
            double sum = 1;
            for (Value v : args) sum *= v.asNumber();
            return new NumVal(sum);
        }));

        env.define("mul?", new BuiltIn(args -> {
            if (args.isEmpty()) {
                throw new RuntimeException("'*' requires at least one argument");
            }
            double sum = 1;
            for (Value v : args) sum *= v.asNumber();
            return new NumVal(sum);
        }));

        // Division
        env.define("/", new BuiltIn(args -> {
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

        env.define("div?", new BuiltIn(args -> {
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



        env.define("%", new BuiltIn(args -> {
            if (args.size() != 2) {
                throw new RuntimeException("'%' requires exactly 2 arguments");
            }
            double a = args.get(0).asNumber();
            double b = args.get(1).asNumber();
            return new NumVal(a % b);
        }));



        env.define("not", new BuiltIn(args -> {
            if (args.size() != 1) {
                throw new RuntimeException("'%' requires exactly 1 argument");
            }
            boolean b = ((BoolVal) args.get(0)).asBoolean();
            return new BoolVal(!b);
        }));

        env.define("=", new BuiltIn(args -> {
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

        env.define("equals?", new BuiltIn(args -> {
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

        env.define("<", new BuiltIn(args -> {
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

        env.define("less-than?", new BuiltIn(args -> {
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

        env.define(">", new BuiltIn(args -> {
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

        env.define("<=", new BuiltIn(args -> {
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

        env.define(">=", new BuiltIn(args -> {
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
        env.define("display", new BuiltIn(args -> {
            for (Value v : args) {
                System.out.print(v);
            }
            return new BoolVal(true); // convention: return #t
        }));
    }
}
