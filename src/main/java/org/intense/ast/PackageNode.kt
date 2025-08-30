package org.intense.ast;

import org.intense.Env
import org.intense.Types.Value

class PackageNode(private var __package__: AtomNode) : ASTNode() {
    override fun eval(env: Env?): Value? {
        TODO("Not yet implemented")
    }


}
