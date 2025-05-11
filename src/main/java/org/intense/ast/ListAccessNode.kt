package org.intense.ast;

import org.intense.SymbolTable;
import org.intense.Types.IntType

// list[:key]
class ListAccessNode(value: String, DataListNode: DataListNode) : ASTNode(){
    lateinit var key:String;
    lateinit var list:ASTNode

    override fun inferType(env: SymbolTable) = IntType()

    override fun toKotlinCode(env: SymbolTable): String {
        TODO("Not yet implemented")
    }
}
