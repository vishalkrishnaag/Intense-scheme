package org.intense.ast;

import org.intense.Env
import org.intense.Types.StrVal
import org.intense.Types.Value

class EnumNode(atom: AtomNode, dataListNode: MutableList<ASTNode>) : ASTNode() {
    private var body:MutableList<ASTNode>? = dataListNode
    private var name:AtomNode = atom


    override fun eval(env: Env): Value {
        val output = StringBuilder()
        if (body!!.isNotEmpty()) {
            for (it in body!!.indices)
            {
                output.append(body!![it].eval(env))
                if (it < body!!.lastIndex) {
                    output.append(", ")
                }
            }
        }
        return StrVal(output.toString())

    }
}
