package org.intense.ast;

import org.intense.SymbolTable
import org.intense.Types.Type

class PackageNode(private var __package__: String) : ASTNode() {


    override fun inferType(env: SymbolTable): Type {
        TODO("Not yet implemented")
    }

    override fun toKotlinCode(env: SymbolTable): String {
        return "package ${__package__.trim('\"')}"
    }

}
