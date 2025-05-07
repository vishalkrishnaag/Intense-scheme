package org.intense.ast;

import org.intense.SymbolTable;

class FunNode(atom: AtomNode, DataListNode: DataListNode) : ASTNode() {
    var body:DataListNode? = null
    var name:AtomNode? = null

    override fun inferType(env: SymbolTable): Type {
        return IntType()
    }

    override fun toKotlinCode(env: SymbolTable): String {
        TODO("Not yet implemented")
    }

    override fun eval(env: SymbolTable): String {
        TODO("Not yet implemented")
    }
}
