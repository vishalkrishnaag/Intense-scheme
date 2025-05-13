package org.intense.ast

import org.intense.SymbolTable
import org.intense.Types.Type
import org.intense.TypingTable

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
    override fun inferType(type: TypingTable, env: SymbolTable): Type {
        return dataType.inferType(type,env)
    }

    override fun toKotlinCode(type: TypingTable, env: SymbolTable): String {
        return if (questionable) {
            "${atom.value}:${dataType.value}?"
        } else {
            "${atom.value}:${dataType.value}"
        }
    }
}