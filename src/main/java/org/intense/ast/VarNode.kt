package org.intense.ast;

import org.intense.SymbolTable;
import org.intense.Types.Type

class VarNode(atom: AtomNode, dataType:AtomNode, dataListNode: DataListNode, question:Boolean) : ASTNode() {
    private var body:DataListNode? = dataListNode
    private var name:AtomNode? = atom
    private var questionMark:Boolean = question
    private var dataType:AtomNode = dataType

    override fun inferType(env: SymbolTable): Type {
        TODO("Not yet implemented")
    }

    override fun toKotlinCode(env: SymbolTable): String {
        return if(questionMark) {
            "\nvar "+ name?.value +":"+dataType.value +"? = " + body?.toKotlinCode(env)
        } else {
            "\nvar "+ name?.value +":"+dataType.value +" = " + body?.toKotlinCode(env)
        }
    }
}
