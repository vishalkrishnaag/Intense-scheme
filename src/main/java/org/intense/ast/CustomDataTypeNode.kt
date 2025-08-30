package org.intense.ast;

import org.intense.Env
import org.intense.Types.UnitVal
import org.intense.Types.Value

// key<x<y>> types
class CustomDataTypeNode(value: String, dataListNode: DataTypeNode) : ASTNode() {
    private var key: String = value
    var list: DataTypeNode = dataListNode

    override fun eval(env: Env): Value {
        val code = StringBuilder()
        code.append(list.eval(env))
        return UnitVal()
    }
}
