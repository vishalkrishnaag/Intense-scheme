package org.intense.ast;

import org.intense.SymbolTable;
import org.intense.Symbols.Symbol
import org.intense.Types.Type

class SetNode(atom: AtomNode, dataListNode: DataListNode) : ASTNode() {
    private var body: DataListNode? = dataListNode
    private var name: AtomNode = atom

    override fun inferType(env: SymbolTable): Type {
        val atomic: Symbol = env.lookup(name.value)
        return env.getTypeStore().lookup(atomic.typeId)
    }

    override fun toKotlinCode(env: SymbolTable): String {

          return  "\n" + name.value + " = " + body?.toKotlinCode(env) + "\n"
    }
}
