package org.intense.ast;

import org.intense.SymbolTable;
import org.intense.Types.Type
import org.intense.TypingTable

class DataListNode(elements: MutableList<ASTNode>) : ASTNode() {
    private var elements: List<ASTNode> = elements
    // often represented as [object1, obj2 , obj3 ...]

    override fun inferType(type: TypingTable, env: SymbolTable): Type {
        TODO("Not yet implemented")
    }

    override fun toKotlinCode(type: TypingTable, env: SymbolTable): String {
        val code = StringBuilder()
        for (it in elements)
         code.append(it.toKotlinCode(type, env))
        return code.toString();
    }
}
