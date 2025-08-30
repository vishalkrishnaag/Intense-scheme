package org.intense.ast;

import org.intense.Env
import org.intense.Types.Value

class InterfaceNode(atom: AtomNode, dataListNode: MutableList<ASTNode>) : ASTNode() {
    private var body:MutableList<ASTNode>? = dataListNode
    private var name:AtomNode = atom

    override fun eval(env: Env): Value {
        TODO("interface not getting supported on this version")
    }
}
