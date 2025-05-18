package org.intense.ast;

import org.intense.SymbolTable
import org.intense.Types.Type

// key<x<y>> types
class CustomDataTypeNode(value: String, dataListNode: DataTypeNode) : ASTNode() {
    var key: String = value
    var list: DataTypeNode = dataListNode

    override fun inferType(env: SymbolTable): Type {
        TODO("not yet implemented")
    }

    override fun toKotlinCode(env: SymbolTable): String {
        val code = StringBuilder()
        code.append(list.toKotlinCode(env))
        return "$key<$code>"
    }
}
