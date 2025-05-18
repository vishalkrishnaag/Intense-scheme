package org.intense.ast;

import org.intense.SymbolTable
import org.intense.Types.GenericType
import org.intense.Types.Type

// list[:key]
class ListAccessNode(value: String, dataListNode: DataListNode) : ASTNode(){
    var key:String = value
    var list:ASTNode =dataListNode

    override fun inferType(env: SymbolTable): Type {
        Exception("invalid Type")
        return GenericType()
    }

    override fun toKotlinCode(env: SymbolTable): String {
        TODO("Not yet implemented")
    }
}
