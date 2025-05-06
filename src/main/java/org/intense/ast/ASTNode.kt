package org.intense.ast;


import org.intense.SymbolTable;
import org.intense.TokenType;

abstract class ASTNode {
    abstract fun inferType(env:SymbolTable):Type

    abstract fun toKotlinCode(env:SymbolTable):String

    abstract fun eval(env:SymbolTable):String
    fun getDataType(type1:TokenType,type2:TokenType):TokenType {
        if (type1 == type2) {
            return type1;
        } else if (type1 == TokenType.DOUBLE && type2 == TokenType.BOOLEAN) {
            return TokenType.DOUBLE;
        } else if (type1 == TokenType.BOOLEAN && type2 == TokenType.DOUBLE) {
            return TokenType.DOUBLE;
        } else if (type1 == TokenType.INT && type2 == TokenType.DOUBLE) {
            return TokenType.DOUBLE;
        } else if (type1 == TokenType.DOUBLE && type2 == TokenType.INT) {
            return TokenType.DOUBLE;
        } else if (type1 == TokenType.STRING && type2 == TokenType.INT) {
            return TokenType.STRING;
        } else if (type1 == TokenType.INT && type2 == TokenType.STRING) {
            return TokenType.STRING;
        } else {
            throw RuntimeException("TokenType is invalid");
        }
    }
}
