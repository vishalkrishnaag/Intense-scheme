package org.intense.ast;

import org.intense.SymbolTable;
import org.intense.Types.Type

class MapNode(keyMap: MutableMap<String, ASTNode>) : ASTNode() {
    override fun inferType(env: SymbolTable): Type {
        TODO("Not yet implemented")
    }

    override fun toKotlinCode(env: SymbolTable): String {
        TODO("Not yet implemented")
    }
}
