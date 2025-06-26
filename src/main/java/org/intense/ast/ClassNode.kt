package org.intense.ast;

import org.intense.SymbolTable;
import org.intense.Symbols.Symbol
import org.intense.Types.Type

class ClassNode(atom: AtomNode, dataListNode: MutableList<ASTNode>,extends: MutableList<ASTNode>?) : ASTNode() {
    private var body:MutableList<ASTNode>? = dataListNode
    private var name:AtomNode = atom
    private var extend: MutableList<ASTNode>?= extends

    override fun inferType(env: SymbolTable): Type {
      val atomic: Symbol = env.lookup(name.value)
      return atomic.type
    }

    override fun toKotlinCode(env: SymbolTable): String {
        val output = StringBuilder()
        val extendedClasses = StringBuilder()
        if(extend!=null)
        {
            for (it in extend!!.indices) {
                extendedClasses.append(extend!![it].toKotlinCode(env))
                if (it < extend!!.lastIndex) {
                    extendedClasses.append(", ")
                }
            }
        }

        if (body!!.isNotEmpty()) {
            for (it in body!!)
            {
                output.append(it.toKotlinCode(env))
            }
        }
        if(extendedClasses.isNotEmpty())
        {
            return "\nclass "+ name.value +" : $extendedClasses {\n " + output.toString() + " }"
        }
        else{
            return "\nclass "+ name.value +" {\n " + output.toString() + " }"
        }


    }
}
