package org.intense.ast;

import org.intense.SymbolTable
import org.intense.Types.CustomDataType
import org.intense.Types.Type

// key<x<y>> types
class CustomDataTypeNode(value: String, dataListNode: DataTypeNode) : ASTNode() {
    private var key: String = value
    var list: DataTypeNode = dataListNode

    override fun inferType(env: SymbolTable): Type {
        return CustomDataType()
    }

    override fun toKotlinCode(env: SymbolTable): String {
        val code = StringBuilder()
        code.append(list.toKotlinCode(env))
        return "$key<$code>"
    }
}
