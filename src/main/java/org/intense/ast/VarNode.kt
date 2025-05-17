package org.intense.ast;

import org.intense.SymbolTable;
import org.intense.Types.Type

class VarNode(atom: AtomNode, mDataType:AtomNode, dataListNode: DataListNode, question:Boolean) : ASTNode() {
    private var body:DataListNode? = dataListNode
    private var name:AtomNode = atom
    private var questionMark:Boolean = question
    private var dataType:AtomNode = mDataType

    override fun inferType(env: SymbolTable): Type {
      val atomic:Symbol = env.lookup(name.value)
      return env.getTypeStore().lookup(atomic.typeId)
    }

    override fun toKotlinCode(env: SymbolTable): String {
        return if(questionMark) {
            "\nvar "+ name.value +":"+dataType.value +"? = " + body?.toKotlinCode(env)
        } else {
            "\nvar "+ name.value +":"+dataType.value +" = " + body?.toKotlinCode(env)
        }
    }
}
