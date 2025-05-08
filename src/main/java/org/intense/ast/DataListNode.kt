package org.intense.ast;

import org.intense.SymbolTable;

class DataListNode(elements: MutableList<ASTNode>) : ASTNode() {
    private var elements: List<ASTNode> = elements
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

    override fun eval(env: SymbolTable): String {
        TODO("Not yet implemented")
    }
}
