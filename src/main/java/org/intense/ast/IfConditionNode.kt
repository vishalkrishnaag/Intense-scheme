package org.intense.ast;

import org.intense.SymbolTable;

class IfConditionNode(ifExpr: ASTNode, ifBody: ASTNode, elseBody: ASTNode) : ASTNode(){
     lateinit var if_exp:ASTNode
     lateinit var if_body:ASTNode
    lateinit var  else_body:ASTNode

    override fun inferType(env: SymbolTable): Type {
        TODO("Not yet implemented")
    }

    override fun toKotlinCode(env: SymbolTable): String {
        val expr:String = if_exp.toKotlinCode(env);
        if (expr.isEmpty()) {
            if_body.eval(env)
            else_body.eval(env)
        }
        return "null";
    }

    override fun eval(env: SymbolTable): String {
        TODO("Not yet implemented")
    }
}
