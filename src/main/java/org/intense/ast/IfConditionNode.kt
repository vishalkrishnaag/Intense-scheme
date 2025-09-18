package org.intense.ast;

import org.intense.Env
import org.intense.Types.NullVal
import org.intense.Types.Value

class IfConditionNode(ifExpr: ASTNode, dtifBody: ASTNode, dtelseBody: ASTNode) : ASTNode() {
    private var ifExp: ASTNode = ifExpr
    private var ifBody: ASTNode? = dtifBody
    private var elseBody: ASTNode? = dtelseBody

    override fun eval(env: Env): Value {
        val expr: StringBuilder = StringBuilder()
        expr.append("\nif (" + ifExp.eval(env) + "){")
        if (ifBody != null) {
            expr.append(ifBody?.eval(env))
            expr.append("}")
            if (elseBody != null) {
                expr.append("else {")
                elseBody?.eval(env)
                expr.append("}")
            }
        }
        return NullVal();
    }
}
