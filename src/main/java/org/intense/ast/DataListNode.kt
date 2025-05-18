package org.intense.ast;

import org.intense.SymbolTable;
import org.intense.Types.Type

class DataListNode(mElements: MutableList<ASTNode>) : ASTNode() {
    var elements: List<ASTNode> = mElements
    // often represented as [object1, obj2 , obj3 ...]

    override fun inferType(env: SymbolTable): Type {
        TODO("Not yet implemented")
    }

    override fun toKotlinCode(env: SymbolTable): String {
        val code = StringBuilder()
        for (it in elements)
         code.append(it.toKotlinCode(env))
        return code.toString();
    }
}
