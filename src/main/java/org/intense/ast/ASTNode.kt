package org.intense.ast;


import org.intense.SymbolTable;
import org.intense.Types.Type

abstract class ASTNode {
    abstract fun inferType(env:SymbolTable): Type
    abstract fun toKotlinCode(env:SymbolTable):String
}
