package org.intense.ast

import org.intense.SymbolTable
import org.intense.Types.Type

class ReturnNode(expr: ASTNode) : ASTNode() {
    private var returnExpr = expr
    override fun inferType(env: SymbolTable): Type {
        TODO("Not yet implemented")
    }

    override fun toKotlinCode(env: SymbolTable): String {
        return "\n\treturn "+ returnExpr.toKotlinCode(env)
    }

}