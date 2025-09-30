package org.intense;

import org.intense.Types.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;

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

        env.defineBuiltIn("make-instance", new BuiltIn(args -> {
            String parent = (args.get(0)).asString(); // "p"
            String child  = (args.get(1)).asString(); // "c"

            // copy root
            int childId = env.getSymbolId(parent);
            env.addSymbol(parent,childId);

            // copy dotted children
            for (var entry : env.getSymbol().entrySet()) {
                String key = entry.getKey();
                if (key.startsWith(parent + ".")) {
                    String suffix = key.substring(parent.length()); // ".b" or ".c"
                    env.addSymbol(child + suffix,entry.getValue());
                }
            }

            return new StrVal("#<done>");
        }));

        env.defineBuiltIn("make-copy!", new BuiltIn(args -> {
            String parent = (args.get(0)).asString(); // "p"
            return new ReferenceValue(parent);
        }));

        env.defineBuiltIn("list", new BuiltIn(ListVal::new));
        env.defineBuiltIn("list.add",new BuiltIn(args ->{
            Value list_provided = args.get(0);
            if(list_provided instanceof ListVal list)
            {
                list.value.add(args.get(1));
                return  list;
            }
            throw new RuntimeException(list_provided+" must be a list to perform list.add");

        }));

        env.defineBuiltIn("list.remove",new BuiltIn(args ->{
            Value list_provided = args.get(0);
            if(list_provided instanceof ListVal list)
            {
                list.value.remove((int) args.get(1).asNumber());
                return  list;
            }
            throw new RuntimeException(list_provided+" must be a list to perform list.remove");

        }));

        env.defineBuiltIn("list.get",new BuiltIn(args ->{
            Value list_provided = args.get(0);
            if(list_provided instanceof ListVal list)
            {
               return list.value.get((int) args.get(1).asNumber());
            }
            throw new RuntimeException(list_provided+" must be a list to perform list.get");

        }));

        env.defineBuiltIn("list.size",new BuiltIn(args ->{
            Value list_provided = args.get(0);
            if(list_provided instanceof ListVal list)
            {
                return new NumVal((double) list.value.size());
            }
            throw new RuntimeException(list_provided+" must be a list to perform list.get");

        }));

        env.defineBuiltIn("stack", new BuiltIn(args -> {
            Stack<Value> stack = new Stack<Value>();
            args.forEach(stack::push);
            return new StackVal(stack);
        }));
        env.defineBuiltIn("stack.push",new BuiltIn(args ->{
            Value list_provided = args.get(0);
            if(list_provided instanceof StackVal stack)
            {
                stack.value.push(args.get(1));
                return  stack;
            }
            throw new RuntimeException(list_provided+" must be a stack to perform stack.push");

        }));

        env.defineBuiltIn("stack.pop",new BuiltIn(args ->{
            Value list_provided = args.get(0);
            if(list_provided instanceof StackVal stack)
            {
                stack.value.pop();
                return  stack;
            }
            throw new RuntimeException(list_provided+" must be a stack to perform stack.pop");

        }));

        env.defineBuiltIn("stack.get",new BuiltIn(args ->{
            Value list_provided = args.get(0);
            if(list_provided instanceof StackVal stack)
            {
                return stack.value.get((int) args.get(1).asNumber());
            }
            throw new RuntimeException(list_provided+" must be a stack to perform stack.get");

        }));

        env.defineBuiltIn("stack.size",new BuiltIn(args ->{
            Value list_provided = args.get(0);
            if(list_provided instanceof StackVal stack)
            {
                return new NumVal((double) stack.value.size());
            }
            throw new RuntimeException(list_provided+" must be a list to perform list.get");

        }));



        env.defineBuiltIn("queue", new BuiltIn(args -> {
            Queue<Value> queue = null;
            try {
                queue.addAll(args);
            } catch (NullPointerException n)
            {
                System.out.println("Operation queue creation failed");
            }

            return new QueueVal(queue);
        }));
        env.defineBuiltIn("queue.enqueue",new BuiltIn(args ->{
            Value list_provided = args.get(0);
            if(list_provided instanceof QueueVal queue)
            {
                queue.value.add(args.get(1));
                return  queue;
            }
            throw new RuntimeException(list_provided+" must be a queue to perform queue.push");

        }));

        env.defineBuiltIn("queue.deque",new BuiltIn(args ->{
            Value list_provided = args.get(0);
            if(list_provided instanceof QueueVal queue)
            {
                queue.value.remove();
                return  queue;
            }
            throw new RuntimeException(list_provided+" must be a queue to perform queue.pop");

        }));

        env.defineBuiltIn("queue.peek",new BuiltIn(args ->{
            Value list_provided = args.get(0);
            if(list_provided instanceof QueueVal queue)
            {
                return queue.value.peek();
            }
            throw new RuntimeException(list_provided+" must be a queue to perform queue.peek");

        }));

        env.defineBuiltIn("queue.size",new BuiltIn(args ->{
            Value list_provided = args.get(0);
            if(list_provided instanceof QueueVal q)
            {
                return new NumVal((double) q.value.size());
            }
            throw new RuntimeException(list_provided+" must be a list to perform list.get");

        }));



        env.defineBuiltIn("map", new BuiltIn(args -> {
                  MapVal map = (MapVal) args.getFirst();
                   return new MapVal(map.value);
        }));

        env.defineBuiltIn("map.put",new BuiltIn(args ->{
            Value list_provided = args.get(0);
            if(list_provided instanceof MapVal map)
            {
                MapVal mapVal = (MapVal) args.get(1);
                map.value.putAll(mapVal.value);
                return map;
            }
            throw new RuntimeException(list_provided+" must be a map to perform map.put");
        }));

        env.defineBuiltIn("map.remove",new BuiltIn(args ->{
            Value list_provided = args.get(0);
            if(list_provided instanceof MapVal map)
            {
                map.value.remove((int) args.get(1).asNumber());
                return  map;
            }
            throw new RuntimeException(list_provided+" must be a list to perform list.remove");

        }));

        env.defineBuiltIn("map.get",new BuiltIn(args ->{
            Value list_provided = args.get(0);
            if(list_provided instanceof MapVal map)
            {
                return map.value.get((int) args.get(1).asNumber());
            }

            throw new RuntimeException(list_provided+" must be a list to perform list.get");

        }));

        env.defineBuiltIn("map.size",new BuiltIn(args ->{
            Value list_provided = args.get(0);
            if(list_provided instanceof MapVal map)
            {
                return new NumVal((double) map.value.size());
            }
            throw new RuntimeException(list_provided+" must be a list to perform list.get");

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

        // [COND]
        env.defineBuiltIn("cond", new BuiltIn(args -> {
            Value output = null;
            for (Value v : args) {
                output = v;
            }
            return output; // convention: return #t
        }));

        env.defineBuiltIn("ls", new BuiltIn(args -> {
            ProcessBuilder pb = new ProcessBuilder("ls");
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                // Run built-in commands via cmd.exe
                pb = new ProcessBuilder("cmd.exe", "/c","dir");
            } else {
                // Linux/macOS: split by spaces
                pb = new ProcessBuilder("ls");
            }
            pb.redirectErrorStream(true); // Merge stdout and stderr

            try {
                Process process = pb.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }

                return new StrVal(line);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }));

        env.defineBuiltIn("dir", new BuiltIn(args -> {
            ProcessBuilder pb;
            BufferedReader reader = null;
            // Check if OS is Windows
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                // Run built-in commands via cmd.exe
                pb = new ProcessBuilder("cmd.exe", "/c","dir");

            } else {
                // Linux/macOS: split by spaces
                pb = new ProcessBuilder("ls");
            }
            try {
                reader = new BufferedReader(new InputStreamReader(pb.start().getInputStream()));
                pb.redirectErrorStream(true); // Merge stdout and stderr
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
                return new StrVal(line);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }));

        env.defineBuiltIn("cd", new BuiltIn(args -> {
            ProcessBuilder pb;
            BufferedReader reader = null;
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                // Run built-in commands via cmd.exe
                pb = new ProcessBuilder("cmd.exe", "/c","cd");

            } else {
                // Linux/macOS: split by spaces
                pb = new ProcessBuilder("cd");
            }
            try {
                reader = new BufferedReader(new InputStreamReader(pb.start().getInputStream()));
                pb.redirectErrorStream(true); // Merge stdout and stderr
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
                return new StrVal(line);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }));

        env.defineBuiltIn("cd", new BuiltIn(args -> {
            ProcessBuilder pb;
            BufferedReader reader = null;
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                // Run built-in commands via cmd.exe
                pb = new ProcessBuilder("cmd.exe", "/c","cd");

            } else {
                // Linux/macOS: split by spaces
                pb = new ProcessBuilder("cd");
            }
            try {
                reader = new BufferedReader(new InputStreamReader(pb.start().getInputStream()));
                pb.redirectErrorStream(true); // Merge stdout and stderr
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
                return new StrVal(line);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }));


        env.defineBuiltIn("cd ..", new BuiltIn(args -> {
            ProcessBuilder pb;
            BufferedReader reader = null;
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                // Run built-in commands via cmd.exe
                pb = new ProcessBuilder("cmd.exe", "/c","cd ..");

            } else {
                // Linux/macOS: split by spaces
                pb = new ProcessBuilder("cd ..");
            }
            try {
                reader = new BufferedReader(new InputStreamReader(pb.start().getInputStream()));
                pb.redirectErrorStream(true); // Merge stdout and stderr
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
                return new StrVal(line);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
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
