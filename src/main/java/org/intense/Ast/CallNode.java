package org.intense.Ast;

import org.intense.Builtin;
import org.intense.Environment;

import java.util.List;

public class CallNode extends ASTNode {
    private ASTNode operand;
    private ASTNode operation;

    public CallNode(ASTNode first, ASTNode rest) {
        this.operand = first;
        this.operation = rest;
    }

    @Override
    public Object eval(Environment env) {
        // both handles condition and method call;
        if (!(operand instanceof AtomNode atom)) {
            return new RuntimeException("Expected operator symbol, got: " + operand);
        }

        switch (atom.value) {
            case "+" -> {
                if (!(operation instanceof ListNode listNode))
                    return new RuntimeException("requires at least one input " + operand);
                double result = 0.0;
                for (ASTNode items : listNode.elements) {
                   result+=(double)items.eval(env);
                }
                return result;
            }
            case "-" -> {
                if (!(operation instanceof ListNode listNode))
                    return new RuntimeException("requires at least one input " + operand);
                double result = 0.0;
                for (ASTNode items : listNode.elements) {
                    result+=(double)items.eval(env);
                }
                return result;
            }
            case "*" -> {
                if (!(operation instanceof ListNode listNode))
                    return new RuntimeException("requires at least one input " + operand);
                double result = 0.0;
                for (ASTNode items : listNode.elements) {
                    result*=(double)items.eval(env);
                }
                return result;
            }
            case "/" -> {
                if (!(operation instanceof ListNode listNode))
                    return new RuntimeException("requires at least one input " + operand);
                double result = 0.0;
                for (ASTNode items : listNode.elements) {
                    result/=(double)items.eval(env);
                }
                return result;
            }
            case "%" -> {
                if (!(operation instanceof ListNode listNode))
                    return new RuntimeException("requires at least one input " + operand);
                double result = 0.0;
                for (ASTNode items : listNode.elements) {
                    result%=(double)items.eval(env);
                }
                return result;
            }
            case "^" -> {
                if (!(operation instanceof ListNode listNode))
                    return new RuntimeException("requires at least one input " + operand);
                boolean result = false;
                for (ASTNode items : listNode.elements) {
                    result^=(boolean)items.eval(env);
                }
                return result;
            }
            case "and" -> {
                if (!(operation instanceof ListNode listNode))
                    return new RuntimeException("requires at least one input " + operand);
                boolean
                        result = false;
                for (ASTNode items : listNode.elements) {
                    result = (boolean)items.eval(env);
                }
                return result;
            }
            case "or" -> {
                if (!(operation instanceof ListNode listNode))
                    return new RuntimeException("requires at least one input " + operand);
                boolean
                        result = false;
                for (ASTNode items : listNode.elements) {
                    result = (boolean)items.eval(env);
                }
                return result;
            }
            case "not" -> {
                if (!(operation instanceof ListNode listNode))
                    return new RuntimeException("requires at least one input " + operand);
                boolean
                        result = false;
                for (ASTNode items : listNode.elements) {
                    result = (boolean)items.eval(env);
                }
                return result;
            }
            case "in" -> {
                if (!(operation instanceof ListNode listNode))
                    return new RuntimeException("requires at least one input " + operand);
                double result = 0.0;
                for (ASTNode items : listNode.elements) {
                    result+=(double)items.eval(env);
                }
                return result;
            }
            case "display" ->{
                System.out.println(operation.eval(env));
            }
            default -> {
                // method call
               List<ASTNode> closure = env.lookup(atom.value);
               for ( ASTNode m_closure : closure){
                   m_closure.eval(env);
               }
            }
        }
        return null;
    }
}
