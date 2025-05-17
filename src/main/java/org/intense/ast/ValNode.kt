package org.intense.ast;

import org.intense.SymbolTable;
import org.intense.Types.IntType
import org.intense.Types.Type

class ValNode(atom: AtomNode,MdataType:AtomNode, dataListNode: DataListNode,question:Boolean) : ASTNode() {
    private var body:DataListNode? = dataListNode
    private var name:AtomNode = atom
    private var questionMark:Boolean = question
    private var dataType:AtomNode = MdataType

    override fun inferType(env: SymbolTable): Type {
        return IntType()
    }

    override fun toKotlinCode(env: SymbolTable): String {
        env.defineV(name.value,ValSymbol(0),dataType.inferType(env))
        if(questionMark)
        {
            return "\nval "+ name?.value +":"+dataType.value +"? = " + body?.toKotlinCode(env)
        }else{
            return "\nval "+ name?.value +":"+dataType.value +" = " + body?.toKotlinCode(env)
        }
    }
}
