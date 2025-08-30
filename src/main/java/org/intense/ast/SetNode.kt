package org.intense.ast;

import org.intense.Env
import org.intense.Types.StrVal
import org.intense.Types.Value

class SetNode(atom: AtomNode, dataListNode: DataListNode) : ASTNode() {
    private var body: DataListNode? = dataListNode
    private var name: AtomNode = atom

    override fun eval(env: Env): Value {
        env.define(name.eval(env).toString(), body?.eval(env))
        return StrVal("set!")
    }
}
