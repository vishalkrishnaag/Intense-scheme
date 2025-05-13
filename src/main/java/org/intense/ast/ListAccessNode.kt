package org.intense.ast;

import org.intense.SymbolTable;
import org.intense.Types.IntType
import org.intense.TypingTable

// list[:key]
class ListAccessNode(value: String, DataListNode: DataListNode) : ASTNode(){
    lateinit var key:String;
    lateinit var list:ASTNode

    override fun inferType(type: TypingTable, env: SymbolTable) = IntType()

    override fun toKotlinCode(type: TypingTable, env: SymbolTable): String {
        TODO("Not yet implemented")
    }
}
