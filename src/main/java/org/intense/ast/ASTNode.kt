package org.intense.ast;


import org.intense.SymbolTable;
import org.intense.Types.Type
import org.intense.TypingTable

abstract class ASTNode {
    abstract fun inferType(type:TypingTable,env:SymbolTable): Type
    abstract fun toKotlinCode(type:TypingTable,env:SymbolTable):String
}
