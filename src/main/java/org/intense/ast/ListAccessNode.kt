package org.intense.ast;

import org.intense.Env
import org.intense.Types.Value

// list[:key]
class ListAccessNode(value: String, dataListNode: DataListNode) : ASTNode(){
    var key:String = value
    var list:ASTNode =dataListNode


    override fun eval(env: Env): Value {
        TODO("Not yet implemented")
    }
}
