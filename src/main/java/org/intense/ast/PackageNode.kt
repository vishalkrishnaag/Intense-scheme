package org.intense.ast;

import org.intense.SymbolTable
import org.intense.Types.Type
import org.intense.TypingTable

class PackageNode(private var __package__: String) : ASTNode() {


    override fun inferType(type: TypingTable, env: SymbolTable): Type {
        TODO("Not yet implemented")
    }

    override fun toKotlinCode(type: TypingTable, env: SymbolTable): String {
        return "package ${__package__.trim('\"')}"
    }

}
