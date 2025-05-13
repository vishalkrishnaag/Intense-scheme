package org.intense.ast;

import org.intense.SymbolTable;
import org.intense.Types.Type
import org.intense.TypingTable

class ListNode(private var elements: ASTNode) : ASTNode() {

    //    String getExtracted(List<ASTNode> input) {
//        StringBuilder result = new StringBuilder("( ");
//        for (ASTNode m_exp : input) {
//            if (m_exp instanceof AtomNode) {
//                if (((AtomNode) m_exp).tokenType == TokenType.STRING) {
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

    override fun inferType(type: TypingTable, env: SymbolTable): Type {
        TODO("Not yet implemented")
    }

    override fun toKotlinCode(type: TypingTable, env: SymbolTable): String {
        val code = StringBuilder()
        code.append(elements.toKotlinCode(type, env))
        return code.toString()
    }
}
