package org.intense.ast;

import org.intense.SymbolTable;
import org.intense.TokenType;

import java.util.Optional;

class AtomNode(TokenType: TokenType, value: String) : ASTNode() {
    var type:TokenType?=null
    var value:String? = null


    override fun toString():String {
        return type.toString()+"(" + value + ")";

    }

    override fun inferType(env: SymbolTable): Type {
        TODO("Not yet implemented")
    }

    override fun toKotlinCode(env: SymbolTable): String {
        TODO("Not yet implemented")
    }

    override fun eval(env: SymbolTable): String {
        TODO("Not yet implemented")
    }
}