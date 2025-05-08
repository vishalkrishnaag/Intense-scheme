package org.intense.ast;

import org.intense.SymbolTable;

class ValNode(atom: AtomNode, DataListNode: DataListNode) : ASTNode() {
    var body:DataListNode? = null
    var name:AtomNode? = null

    override fun inferType(env: SymbolTable): Type {
        return IntType()
    }

    override fun toKotlinCode(env: SymbolTable): String {
        return "val "+ name?.toKotlinCode(env) + "= " + body?.toKotlinCode(env)
    }

    override fun eval(env: SymbolTable): String {
        TODO("Not yet implemented")
    }
}
