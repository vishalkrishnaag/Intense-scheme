package org.intense.ast;

import org.intense.SymbolTable
import org.intense.Types.IntType
import org.intense.Types.Type

class ValNode(atom: AtomNode, atomNode:AtomNode?, dataListNode: DataListNode, question:Boolean) : ASTNode() {
    private var body:DataListNode? = dataListNode
    private var name:AtomNode = atom
    private var questionMark:Boolean = question
    private var dataType:AtomNode? = atomNode

    override fun inferType(env: SymbolTable): Type {
        return IntType()
    }

    override fun toKotlinCode(env: SymbolTable): String {
        if(dataType ==null)
        {
            return "\nval "+name.value + " = "+ body?.toKotlinCode(env) + "()\n"
        }
        else{
            return if (questionMark) {
                "\nval " + name.value + ":" + dataType?.value + "? = " + body?.toKotlinCode(env) + "\n"
            } else {
                "\nval " + name.value + ":" + dataType?.value + " = " + body?.toKotlinCode(env) + "\n"
            }
        }
    }
}
