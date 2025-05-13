package org.intense.ast

import org.intense.SymbolTable
import org.intense.Types.Type
import org.intense.TypingTable

class ReturnNode(expr: ASTNode) : ASTNode() {
    private var returnExpr = expr
    override fun inferType(type: TypingTable, env: SymbolTable): Type {
        TODO("Not yet implemented")
    }

    override fun toKotlinCode(type: TypingTable, env: SymbolTable): String {
        return "\n\treturn "+ returnExpr.toKotlinCode(type, env)
    }

}