package org.intense.Ast;

import org.intense.Builtin;
import org.intense.Environment;
import org.intense.TokenType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ListNode extends ASTNode {

    public String toString() {
        return "List(" + elements + ")";
    }

    public final List<ASTNode> elements;

    public ListNode(List<ASTNode> elements) {
        this.elements = elements;
    }

    String getExtracted(List<ASTNode> input) {
        StringBuilder result = new StringBuilder("( ");
        for (ASTNode m_exp : input) {
            if (m_exp instanceof AtomNode) {
                if (((AtomNode) m_exp).type == TokenType.STRING) {
                    result.append(" \"").append(((AtomNode) m_exp).value).append("\"");
                } else result.append(((AtomNode) m_exp).value).append(" \n");
            } else if (m_exp instanceof ListNode) {
                result.append(" ").append(getExtracted(((ListNode) m_exp).elements));
            } else {
                System.out.println("exp : " + m_exp);
                result.append(m_exp.toString());
            }

        }
        result.append(" )");
        return result.toString();
    }

    @Override
    public Object eval(Environment env) {
        if (elements.isEmpty()) return null;
        ASTNode first = elements.getFirst();
       return switch (first)
        {
            case DataListNode dl -> dl.eval(env);
            case MapNode mp -> mp.eval(env);
            case DefNode def -> def.eval(env);
            case ListNode listNode -> listNode.eval(env);
            case IfConditionNode If -> If.eval(env);
            case CallNode callNode -> callNode.eval(env);
            case PackageNode packageNode -> packageNode.eval(env);
            case SetNode setNode -> setNode.eval(env);
            default -> {
                System.err.println("unknown operation found "+ first);
                yield null;
            }
        };

    }
}
