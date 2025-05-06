package org.intense.ast;

import org.intense.SymbolTable;

class ListNode(var elements: ASTNode) : ASTNode() {

    //    String getExtracted(List<ASTNode> input) {
//        StringBuilder result = new StringBuilder("( ");
//        for (ASTNode m_exp : input) {
//            if (m_exp instanceof AtomNode) {
//                if (((AtomNode) m_exp).type == TokenType.STRING) {
//                    result.append(" \"").append(((AtomNode) m_exp).value).append("\"");
//                } else result.append(((AtomNode) m_exp).value).append(" \n");
//            } else if (m_exp instanceof ListNode) {
//                result.append(" ").append(getExtracted(((ListNode) m_exp).elements));
//            } else {
//                System.out.println("exp : " + m_exp);
//                result.append(m_exp.toString());
//            }
//
//        }
//        result.append(" )");
//        return result.toString();
//    }

    override fun inferType(env: SymbolTable): Type {
        TODO("Not yet implemented")
    }

    override fun toKotlinCode(env: SymbolTable): String {
        TODO("Not yet implemented")
    }

    override fun eval(env: SymbolTable): String {
        //        return if (elements)
//        {
//            case DataListNode dl -> dl.eval(env);
//            case MapNode mp -> mp.eval(env);
//            case DefNode def -> def.eval(env);
//            case ListNode listNode -> listNode.eval(env);
//            case IfConditionNode If -> If.eval(env);
//            case CallNode callNode -> callNode.eval(env);
//            case PackageNode packageNode -> packageNode.eval(env);
//            case RequiredNode required -> required.eval(env);
//            default -> {
//            System.err.println("unknown operation found "+ elements);
//            yield null;
//        }
//        };
        return "";
    }
}
