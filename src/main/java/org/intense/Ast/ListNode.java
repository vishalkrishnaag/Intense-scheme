package org.intense.Ast;

import org.intense.Closure;
import org.intense.Environment;
import org.intense.TokenType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ListNode extends ASTNode {

    public String toString() {
        return "List(" + elements + ")";
    }

    public final List<ASTNode> elements;

    public ListNode(List<ASTNode> elements) {
        this.elements = elements;
    }

    @Override
    public Result<Object> eval(Environment env) {
        if (elements.isEmpty()) return Result.ok(null);

        ASTNode first = elements.get(0);
        if (!(first instanceof AtomNode atom)) {
            return Result.error(new RuntimeException("Expected operator symbol, got: " + first));
        }

        String op = atom.value;
        int size = elements.size();

        try {
            return switch (op) {
                case "define" -> {
                    if (size < 3) throw new IllegalArgumentException("Malformed define expression");
                    String name = ((AtomNode) elements.get(1)).value;
                    ASTNode second = elements.get(2);

                    if (second instanceof ListNode params) {
                        if (size < 4) throw new IllegalArgumentException("Function definition missing body");
                        ASTNode body = elements.get(3);
                        Closure function = new Closure(params, body, env);
                        env.define(name, function);
                        yield Result.ok(function);
                    } else {
                        Object value = second.eval(env);
                        env.define(name, value);
                        yield Result.ok(value);
                    }
                }
                case "if" -> {
                    if (size != 4) throw new IllegalArgumentException("Invalid if expression, expected 3 operands");
                    Result<Object> condition = elements.get(1).eval(env);
                    boolean result = condition.isSuccess();
                    yield Result.ok((result)
                            ? elements.get(2).eval(env)
                            : elements.get(3).eval(env));
                }
                case "lambda" -> {
                    if (size != 3) throw new IllegalArgumentException("Malformed lambda expression");
                    ASTNode rawParams = elements.get(1);
                    if (!(rawParams instanceof ListNode paramList)) {
                        throw new IllegalArgumentException("Expected list of parameters in lambda");
                    }
                    yield Result.ok(new Closure(paramList, elements.get(2), env));
                }
                // same for "begin", "quote", "display", "list"...

                case "+", "-", "*", "/" -> {
                    if (size < 3) throw new IllegalArgumentException(op + " expects at least 2 operands");
                    Object firstEval = elements.get(1).eval(env);
                    if (!(firstEval instanceof Number)) throw new IllegalArgumentException("Non-numeric operand");

                    double result = ((Number) firstEval).doubleValue();
                    for (int i = 2; i < size; i++) {
                        Object nextEval = elements.get(i).eval(env);
                        if (!(nextEval instanceof Number)) throw new IllegalArgumentException("Non-numeric operand");
                        double next = ((Number) nextEval).doubleValue();

                        switch (op) {
                            case "+" -> result += next;
                            case "-" -> result -= next;
                            case "*" -> result *= next;
                            case "/" -> {
                                if (next == 0) throw new ArithmeticException("Division by zero");
                                result /= next;
                            }
                        }
                    }
                    yield Result.ok(result);
                }

                default -> {
                    if (atom.type == TokenType.SYMBOL) {
                        Optional<Closure> closure = env.get(atom.value);
                        if (closure.isPresent()) {
                            List<Object> args = new ArrayList<>();
                            for (int i = 1; i < size; i++) {
                                args.add(elements.get(i).eval(env));
                            }
                            Optional<Object> result = closure.get().apply(args);
                            yield result.map(Result::ok).orElseGet(() -> Result.error(new RuntimeException("Closure application failed")));
                        }
                        yield Result.ok(first.eval(env));
                    } else {
                        throw new RuntimeException("Unknown operator: " + op);
                    }
                }
            };
        } catch (Exception ex) {
            return Result.error(ex);
        }
    }

}


