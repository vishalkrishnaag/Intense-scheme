package org.intense.ast;

import org.intense.SymbolTable;
import org.intense.Types.Type
import org.intense.TypingTable

class LinkingNode : ASTNode(){
    var elements: List<ASTNode>? = null
    /**
     * .b.c tokenType is linking Node
     * primary functions are object life cycles
     * */

    override fun inferType(type: TypingTable, env: SymbolTable): Type {
        TODO("Not yet implemented")
    }

    override fun toKotlinCode(type: TypingTable, env: SymbolTable): String {
        TODO("Not yet implemented")
    }
}
