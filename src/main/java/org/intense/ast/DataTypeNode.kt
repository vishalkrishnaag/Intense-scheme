package org.intense.ast;

import org.intense.Env
import org.intense.Types.StrVal
import org.intense.Types.Value

// key<x<y>> types
class DataTypeNode(dataListNode: MutableList<ASTNode>) : ASTNode() {
    var list: MutableList<ASTNode> = dataListNode

    override fun eval(env: Env): Value {
        val code = StringBuilder()
        for (it in list) {
            if (it is DataTypeNode) {
//                val codec = it.eval(env)
//                if (codec.isNotEmpty()) {
//                    code.append("<")
//                    code.append(codec)
//                    code.append(">")
//                }
                code.append("<")
                code.append(it.eval(env))
                code.append(">")

            } else {
                code.append(it.eval(env))
            }
        }
        return StrVal(code.toString())
    }
}
