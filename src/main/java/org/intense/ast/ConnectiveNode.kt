package org.intense.ast

import org.intense.Env
import org.intense.Types.StrVal
import org.intense.Types.Value

class ConnectiveNode(mName:String) : ASTNode() {
    // is
    var name:String = mName
    override fun eval(env: Env): Value {
        return StrVal("")
    }
}