package org.intense.ast;

import org.intense.SymbolTable;
import org.intense.Types.Type

class WhileConditionNode(whileExpr: ASTNode, whileBody: ASTNode) : ASTNode() {
    private var whlExp: ASTNode = whileExpr
    private var whlBody: ASTNode? = whileBody

    override fun inferType(env: SymbolTable): Type {
        TODO("Not yet implemented")
    }

    override fun toKotlinCode(env: SymbolTable): String {
        val expr: StringBuilder = StringBuilder()
        expr.append("\nwhile (" + whlExp.toKotlinCode(env) + "){")
        expr.append(whlBody?.toKotlinCode(env))
        expr.append("}")
        return expr.toString();
    }
}
