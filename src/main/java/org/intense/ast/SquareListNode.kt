package org.intense.ast;

import org.intense.Env
import org.intense.Types.Value

class SquareListNode(elements: MutableList<ASTNode>) : ASTNode() {
    private var elements: List<ASTNode> = elements
    // often represented as [object1, obj2 , obj3 ...]

    override fun eval(env: Env): Value {
        TODO("not implemented properly ")
    }
}
