package org.intense.ast;

import org.intense.SymbolTable;
import org.intense.Types.Type

class SquareListNode(elements: MutableList<ASTNode>) : ASTNode() {
    private var elements: List<ASTNode> = elements
    // often represented as [object1, obj2 , obj3 ...]

    override fun inferType(env: SymbolTable): Type {
        TODO("Not yet implemented")
    }

    override fun toKotlinCode(env: SymbolTable): String {
        val code = StringBuilder()
        for (it in elements.indices) {
            code.append(elements[it].toKotlinCode(env))
            if (it < elements.lastIndex) {
                code.append(", ")
            }
        }
        return "mutableListOf($code)";
    }
}
