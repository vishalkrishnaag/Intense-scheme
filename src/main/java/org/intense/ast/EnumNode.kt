package org.intense.ast;

import org.intense.SymbolTable;
import org.intense.Symbols.Symbol
import org.intense.Types.Type

class EnumNode(atom: AtomNode, dataListNode: MutableList<ASTNode>) : ASTNode() {
    private var body:MutableList<ASTNode>? = dataListNode
    private var name:AtomNode = atom

    override fun inferType(env: SymbolTable): Type {
      val atomic: Symbol = env.lookup(name.value)
      return atomic.type
    }

    override fun toKotlinCode(env: SymbolTable): String {
        val output = StringBuilder()
        if (body!!.isNotEmpty()) {
            for (it in body!!.indices)
            {
                output.append(body!![it].toKotlinCode(env))
                if (it < body!!.lastIndex) {
                    output.append(", ")
                }
            }
        }
        return "\nenum class "+ name.value +" {\n " + output.toString() + " }"

    }
}
