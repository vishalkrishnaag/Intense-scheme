package org.intense.ast;

import org.intense.SymbolTable;

class LinkingNode : ASTNode(){
    var elements: List<ASTNode>? = null
    /**
     * .b.c type is linking Node
     * primary functions are object life cycles
     * */

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
