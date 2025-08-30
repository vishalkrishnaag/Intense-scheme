package org.intense.ast;

import org.intense.Env
import org.intense.Types.StrVal
import org.intense.Types.Value

class DataListNode(mElements: MutableList<ASTNode>) : ASTNode() {
    private var elements: List<ASTNode> = mElements
    // often represented as [object1, obj2 , obj3 ...]

    override fun eval(env: Env): Value {
        val code = StringBuilder()
        for (it in elements)
         code.append(it.eval(env))
        return StrVal(code.toString())
    }
}
