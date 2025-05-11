package org.intense.ast;

import org.intense.SymbolTable;
import org.intense.Types.Type

class IfConditionNode(ifExpr: ASTNode, dtifBody: ASTNode, dtelseBody: ASTNode) : ASTNode(){
      private var ifExp:ASTNode = ifExpr
      private var ifBody:ASTNode = dtifBody
      private var  elseBody:ASTNode = dtelseBody

    override fun inferType(env: SymbolTable): Type {
        TODO("Not yet implemented")
    }

    override fun toKotlinCode(env: SymbolTable): String {
        val expr:StringBuilder= StringBuilder()
        expr.append("\nif ("+ifExp.toKotlinCode(env)+"){")
        if (expr.isEmpty()) {
           expr.append(ifBody.toKotlinCode(env))
           expr.append("}")
            expr.append("else {")
            elseBody.toKotlinCode(env)
            expr.append("}")
        }
        return expr.toString();
    }
}
