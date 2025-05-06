package org.intense.ast;

import org.intense.SymbolTable;

class CallNode(var operand: ASTNode?, var params: ASTNode?) : ASTNode() {

    override fun inferType(env: SymbolTable): Type {
        TODO("Not yet implemented")
    }

    override fun toKotlinCode(env: SymbolTable): String {
        TODO("Not yet implemented")
    }

    override fun eval(env: SymbolTable): String {
        TODO("Not yet implemented")
    }

}
