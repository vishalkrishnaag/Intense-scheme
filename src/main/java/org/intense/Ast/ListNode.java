package org.intense.Ast;

import org.intense.Builtin;
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

    public Object apply(List<ASTNode> args, List<ASTNode> body) {
        try {
            Object result = null;
            //1 st is lambda
            for (ASTNode astNode : body) {
                result = astNode.eval();
            }
            return Optional.ofNullable(result);
        } catch (Exception e) {
            throw new RuntimeException("closure application failed");
        }
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
        if (op.equals("quote")) {
            StringBuilder result = new StringBuilder("(");
            for (ASTNode exp : elements) {
                if (exp instanceof AtomNode) {
                    result.append(((AtomNode) exp).value).append(" \n");
                } else if (exp instanceof ListNode) {
                    result.append(getExtracted((ListNode) exp));
                } else {
                    System.out.println("exp : " + exp);
                    result.append(exp.toString());
                }

            }
            result.append(")");
            return result.toString();
        }
       else if(op.equals("def")) {
            if (size < 3) throw new IllegalArgumentException("Malformed define expression");
            String name = ((AtomNode) elements.get(1)).value;
            List<ASTNode> function = new ArrayList<>(elements.subList(1, elements.size()));
            env.define(name,function);
            return  "(closure)";
        }
        else {
           Builtin builtin = new Builtin(op, size, first, atom, elements);
            Object result = Builtin.call();
            if (atom.type == TokenType.SYMBOL && result==null) {
                List<ASTNode> closure = env.lookup(atom.value);
                if (closure != null) {
                    List<ASTNode> args = new ArrayList<>(elements.subList(1, elements.size()));
                    return apply(args, closure);
                }
                return first.eval();
            } else {
                return result;
            }
        }
    }

}


