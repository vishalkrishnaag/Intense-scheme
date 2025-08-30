package org.intense.ast;

import org.intense.Env
import org.intense.Types.StrVal
import org.intense.Types.Value

class WhileConditionNode(whileExpr: ASTNode, whileBody: ASTNode) : ASTNode() {
    private var whlExp: ASTNode = whileExpr
    private var whlBody: ASTNode? = whileBody

    override fun eval(env: Env): Value {
        return StrVal("something went wrong");
    }
}
