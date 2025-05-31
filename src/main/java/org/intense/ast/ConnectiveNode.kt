package org.intense.ast

import org.intense.SymbolTable
import org.intense.Types.Type

class ConnectiveNode(mName:String) : ASTNode() {
    // is
    var name:String = mName

    override fun inferType(env: SymbolTable): Type {
        TODO("Not yet implemented")
    }

    override fun toKotlinCode(env: SymbolTable): String {
        return ""
    }
}