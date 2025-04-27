package org.intense.Ast;

import org.intense.Environment;

import java.util.List;
import java.util.Map;

public class CallNode extends ASTNode {
    private ASTNode operand;
    private ASTNode params;

    public CallNode(ASTNode first, ASTNode rest) {
        this.operand = first;
        this.params = rest;
    }

    @Override
    public Object eval(Environment env) {
        // both handles condition and method call;
        if (!(operand instanceof AtomNode atom)) {
            return new RuntimeException("Expected operator symbol, got: " + operand);
        }

        switch (atom.value) {
            case "+" -> {
                if (!(params instanceof ListNode listNode))
                    return new RuntimeException("requires at least one input " + operand);
                double result = 0.0;
                for (ASTNode items : listNode.elements) {
                   result+=(double)items.eval(env);
                }
                return result;
            }
            case "-" -> {
                if (!(params instanceof ListNode listNode))
                    return new RuntimeException("requires at least one input " + operand);
                double result = 0.0;
                for (ASTNode items : listNode.elements) {
                    result+=(double)items.eval(env);
                }
                return result;
            }
            case "*" -> {
                if (!(params instanceof ListNode listNode))
                    return new RuntimeException("requires at least one input " + operand);
                double result = 0.0;
                for (ASTNode items : listNode.elements) {
                    result*=(double)items.eval(env);
                }
                return result;
            }
            case "/" -> {
                if (!(params instanceof ListNode listNode))
                    return new RuntimeException("requires at least one input " + operand);
                double result = 0.0;
                for (ASTNode items : listNode.elements) {
                    result/=(double)items.eval(env);
                }
                return result;
            }
            case "%" -> {
                if (!(params instanceof ListNode listNode))
                    return new RuntimeException("requires at least one input " + operand);
                double result = 0.0;
                for (ASTNode items : listNode.elements) {
                    result%=(double)items.eval(env);
                }
                return result;
            }
            case "^" -> {
                if (!(params instanceof ListNode listNode))
                    return new RuntimeException("requires at least one input " + operand);
                boolean result = false;
                for (ASTNode items : listNode.elements) {
                    result^=(boolean)items.eval(env);
                }
                return result;
            }
            case "and" -> {
                if (!(params instanceof ListNode listNode))
                    return new RuntimeException("requires at least one input " + operand);
                boolean
                        result = false;
                for (ASTNode items : listNode.elements) {
                    result = (boolean)items.eval(env);
                }
                return result;
            }
            case "or" -> {
                if (!(params instanceof ListNode listNode))
                    return new RuntimeException("requires at least one input " + operand);
                boolean
                        result = false;
                for (ASTNode items : listNode.elements) {
                    result = (boolean)items.eval(env);
                }
                return result;
            }
            case "not" -> {
                if (!(params instanceof ListNode listNode))
                    return new RuntimeException("requires at least one input " + operand);
                boolean
                        result = false;
                for (ASTNode items : listNode.elements) {
                    result = (boolean)items.eval(env);
                }
                return result;
            }
            case "in" -> {
                if (!(params instanceof ListNode listNode))
                    return new RuntimeException("requires at least one input " + operand);
                double result = 0.0;
                for (ASTNode items : listNode.elements) {
                    result+=(double)items.eval(env);
                }
                return result;
            }
            case "display" ->{
                System.out.println(params.eval(env));
            }
            default -> {
                // method call
                if((params instanceof MapNode argumentMap))
                {
                    for (Map.Entry<String, ASTNode> arguments : argumentMap.object.entrySet())
                    {
                      env.define(arguments.getKey(),arguments.getValue());
                    }
                }
               ASTNode closure = env.lookup(atom.value);
                closure.eval(env);
            }
        }
        return null;
    }
}
