package org.intense.ast;

import org.intense.SymbolTable;
import org.intense.Types.Type

class ObjectNode : ASTNode(){
    /*
    * it is used to store and process class objects*/
    override fun inferType(env: SymbolTable): Type {
        TODO("Not yet implemented")
    }

    override fun toKotlinCode(env: SymbolTable): String {
        TODO("Not yet implemented")
    }

}
