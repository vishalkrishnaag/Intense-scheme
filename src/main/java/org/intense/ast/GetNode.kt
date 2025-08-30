package org.intense.ast;

import org.intense.Env
import org.intense.Types.Value

class GetNode(input:ASTNode) : ASTNode() {
    private var body:ASTNode = input

    override fun eval(env: Env): Value {
          return body.eval(env)
    }
}
