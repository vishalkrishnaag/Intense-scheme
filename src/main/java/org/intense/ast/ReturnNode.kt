package org.intense.ast

import org.intense.Env
import org.intense.Types.Value

class ReturnNode(expr: ASTNode) : ASTNode() {
    private var returnExpr = expr

    override fun eval(env: Env): Value {
        return returnExpr.eval(env)
    }

}