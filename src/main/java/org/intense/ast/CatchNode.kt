package org.intense.ast;

import org.intense.SymbolTable
import org.intense.Types.IntType
import org.intense.Types.Type

class CatchNode(atom: AtomNode, atomNode: ASTNode?, dataListNode: DataListNode) : ASTNode() {
    private var body: DataListNode? = dataListNode
    private var name: AtomNode = atom
    private var dataType: ASTNode? = atomNode

    override fun inferType(env: SymbolTable): Type {
        return IntType()
    }

    override fun toKotlinCode(env: SymbolTable): String {
        if (dataType == null) {
            throw Exception("catch block expect an Exception type")
        } else {
            return "\ncatch  (" + name.value + ":" + dataType?.toKotlinCode(env) + ") { " + body?.toKotlinCode(env) + "}\n"
        }

    }
}
