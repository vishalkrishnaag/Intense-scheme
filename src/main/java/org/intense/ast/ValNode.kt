package org.intense.ast;

import org.intense.SymbolTable;
import org.intense.Types.IntType
import org.intense.Types.Type
import org.intense.TypingTable

class ValNode(atom: AtomNode,MdataType:AtomNode, dataListNode: DataListNode,question:Boolean) : ASTNode() {
    private var body:DataListNode? = dataListNode
    private var name:AtomNode? = atom
    private var questionMark:Boolean = question
    private var dataType:AtomNode = MdataType

    override fun inferType(type: TypingTable, env: SymbolTable): Type {
        return IntType()
    }

    override fun toKotlinCode(type: TypingTable, env: SymbolTable): String {
        if(questionMark)
        {
            return "\nval "+ name?.value +":"+dataType.value +"? = " + body?.toKotlinCode(type, env)
        }else{
            return "\nval "+ name?.value +":"+dataType.value +" = " + body?.toKotlinCode(type, env)
        }
    }
}
