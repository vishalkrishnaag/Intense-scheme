package org.intense.ast;

import org.intense.SymbolTable;
import org.intense.Types.Type
import org.intense.TypingTable

class IfConditionNode(ifExpr: ASTNode, dtifBody: ASTNode, dtelseBody: ASTNode) : ASTNode() {
    private var ifExp: ASTNode = ifExpr
    private var ifBody: ASTNode? = dtifBody
    private var elseBody: ASTNode? = dtelseBody

    override fun inferType(type: TypingTable, env: SymbolTable): Type {
        TODO("Not yet implemented")
    }

    override fun toKotlinCode(type: TypingTable, env: SymbolTable): String {
        val expr: StringBuilder = StringBuilder()
        expr.append("\nif (" + ifExp.toKotlinCode(type, env) + "){")
        if (ifBody != null) {
            expr.append(ifBody?.toKotlinCode(type, env))
            expr.append("}")
            if (elseBody != null) {
                expr.append("else {")
                elseBody?.toKotlinCode(type, env)
                expr.append("}")
            }
        }
        return expr.toString();
    }
}
