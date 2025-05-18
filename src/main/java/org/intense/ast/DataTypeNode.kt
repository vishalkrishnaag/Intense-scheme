package org.intense.ast;

import org.intense.SymbolTable
import org.intense.Types.Type

// key<x<y>> types
class DataTypeNode(dataListNode: MutableList<ASTNode>) : ASTNode() {
    var list: MutableList<ASTNode> = dataListNode

    override fun inferType(env: SymbolTable): Type {
        TODO("not yet implemented")
    }

    override fun toKotlinCode(env: SymbolTable): String {
        val code = StringBuilder()
        for (it in list) {
            if (it is DataTypeNode) {
//                val codec = it.toKotlinCode(env)
//                if (codec.isNotEmpty()) {
//                    code.append("<")
//                    code.append(codec)
//                    code.append(">")
//                }
                code.append("<")
                code.append(it.toKotlinCode(env))
                code.append(">")

            } else {
                code.append(it.toKotlinCode(env))
            }
        }
        return code.toString()
    }
}
