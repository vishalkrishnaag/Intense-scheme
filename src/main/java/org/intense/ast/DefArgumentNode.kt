package org.intense.ast

import org.intense.SymbolTable
import org.intense.Types.Type

class DefArgumentNode(
    argAtom: AtomNode,
    argDataType: AtomNode,
    question: Boolean
) : ASTNode() {
    /*
    * A single argument like a:Int 100 is captured here */
    private var atom: AtomNode = argAtom
    private var dataType: AtomNode = argDataType
    private var questionable: Boolean = question
    override fun inferType(env: SymbolTable): Type {
        TODO("Not yet implemented")
    }

    override fun toKotlinCode(env: SymbolTable): String {
        if (questionable) {
            return "${atom.value}:${dataType.value}?"
        } else {
            return "${atom.value}:${dataType.value}"
        }
    }
}