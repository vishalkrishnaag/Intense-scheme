package org.intense.ast;

import org.intense.Env
import org.intense.Types.Value

class MapNode(keyMap: MutableMap<ASTNode, ASTNode>) : ASTNode() {

    override fun eval(env: Env): Value {
        TODO("Not yet implemented")
    }
}
