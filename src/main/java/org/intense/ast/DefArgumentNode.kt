package org.intense.ast

import org.intense.SymbolTable
import org.intense.Types.Type

class DefArgumentNode(
    argAtom: AtomNode,
    argDataType: ASTNode,
    question: Boolean
) : ASTNode() {
    /*
    * A single argument like a:Int 100 is captured here */
    private var atom: AtomNode = argAtom
    private var dataType: ASTNode = argDataType
    private var questionable: Boolean = question
    override fun inferType(env: SymbolTable): Type {
        return dataType.inferType(env)
    }

    override fun toKotlinCode(env: SymbolTable): String {
        return if (questionable) {
            "${atom.value}:${dataType.toKotlinCode(env)}?"
        } else {
            "${atom.value}:${dataType.toKotlinCode(env)}"
        }
    }
}