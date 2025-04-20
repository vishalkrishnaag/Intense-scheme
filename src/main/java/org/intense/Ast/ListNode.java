package org.intense.Ast;

import org.intense.Closure;
import org.intense.TokenType;

import java.util.ArrayList;
import java.util.List;

public class ListNode extends ASTNode {

    public String toString() {
        return "List(" + elements + ")";
    }

    public final List<ASTNode> elements;

    public ListNode(List<ASTNode> elements) {
        this.elements = elements;
    }

    String getExtracted(ListNode input) {
        StringBuilder result = new StringBuilder("( ");
        for (ASTNode m_exp : input.elements) {
            if (m_exp instanceof AtomNode) {
                if (((AtomNode) m_exp).type == TokenType.STRING) {
                    result.append(" \"").append(((AtomNode) m_exp).value).append("\"");
                } else
                    result.append(((AtomNode) m_exp).value).append(" \n");
            } else if (m_exp instanceof ListNode) {
                result.append(" ").append(getExtracted((ListNode) m_exp));
            } else {
                System.out.println("exp : " + m_exp);
                result.append(m_exp.toString());
            }

        }
        result.append(" )");
        return result.toString();
    }

    @Override
    public Object eval() {
        if (elements.isEmpty()) return null;

        ASTNode first = elements.get(0);
        if (!(first instanceof AtomNode atom)) {
            return new RuntimeException("Expected operator symbol, got: " + first);
        }

        String op = atom.value;
        int size = elements.size();

        try {
            return switch (op) {
                case "define" -> {
                    if (size < 3) throw new IllegalArgumentException("Malformed define expression");
                    String name = ((AtomNode) elements.get(1)).value;
                    ASTNode second = elements.get(2);
                    Object value = second.eval();
                    env.define(name, value);
                    yield value;
                }
                case "lambda" -> {
                    if (size < 3) {
                        yield new Exception("Malformed lambda: expected (lambda (params) body)");
                    }
                    List<ASTNode> mBody = new ArrayList<>(elements.subList(1, elements.size()));
                    yield new Closure(mBody);
                }
                case "if" -> {
                    if (size != 4) throw new IllegalArgumentException("Invalid if expression, expected 3 operands");
                    Object condition = elements.get(1).eval();
                    boolean result = condition != null && condition != (Boolean) false;
                    yield (result
                            ? elements.get(2).eval()
                            : elements.get(3).eval());
                }
                case "begin" -> {
                    Object result = null;
                    for (int i = 1; i < size; i++) {
                        result = elements.get(i).eval();
                    }
                    yield result;
                }

                case "quote" -> {
                    String result = "(";
                    for (ASTNode exp : elements) {
                        if (exp instanceof AtomNode) {
                            result += ((AtomNode) exp).value + " \n";
                        } else if (exp instanceof ListNode) {
                            result += getExtracted((ListNode) exp);
                        } else {
                            System.out.println("exp : " + exp);
                            result += exp.toString();
                        }

                    }
                    result += ")";
                    yield result;
                }

                case "display" -> {
                    if (size < 2) yield new RuntimeException("display expects exactly 1 argument");
                    Object val = elements.get(1).eval();
                    System.out.println(val);
                    yield "()";
                }

                case "list" -> {
                    yield elements.get(1).eval();

                }
                case "+", "-", "*", "/" -> {
                    if (size < 3) throw new IllegalArgumentException(op + " expects at least 2 operands");
                    Object firstEval = elements.get(1).eval();
                    if (!(firstEval instanceof Number)) throw new IllegalArgumentException("Non-numeric operand");

                    double result = ((Number) firstEval).doubleValue();
                    for (int i = 2; i < size; i++) {
                        Object nextEval = elements.get(i).eval();
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
                    yield result;
                }

                default -> {
                    if (atom.type == TokenType.SYMBOL) {
                        Closure closure = env.get(atom.value);
                        if (closure!=null) {
                            Object result = closure.apply();
                            yield result !=null ? result : new RuntimeException("Closure application failed");
                        }
                        yield first.eval();
                    } else {
                        throw new RuntimeException("Unknown operator: " + op);
                    }
                }
            };
        } catch (Exception ex) {
            return ex;
        }
    }

}


