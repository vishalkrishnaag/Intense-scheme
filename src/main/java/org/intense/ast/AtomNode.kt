package org.intense.ast;

import org.intense.SymbolTable;
import org.intense.TokenType;
import org.intense.Types.*

class AtomNode(tokenType: TokenType, var value: String) : ASTNode() {
    var type: TokenType? = tokenType


    override fun toString(): String {
        return type.toString()+ "(" + value + ")";

    }

    override fun inferType(env: SymbolTable): Type {
        //Todo: symbol needs separate treatment
        return when (type) {
            TokenType.NUMBER -> IntType()
            TokenType.NONE -> NoneType()
            TokenType.STRING -> StringType()
            TokenType.BOOLEAN -> BooleanType()
            TokenType.EOF -> throw Exception("code exited with partial execution")
            TokenType.THIS -> ThisNode()
            TokenType.INT -> IntType()
            TokenType.FLOAT -> FloatType()
            TokenType.LONG -> LongType()
            TokenType.DOUBLE -> DoubleType()
            TokenType.OBJECT -> ObjectType()
            TokenType.NULL -> NoneType()
            TokenType.BYTE -> ByteType()
            TokenType.SHORT -> ShortType()
            TokenType.UBYTE -> UByteType()
            TokenType.ULONG -> ULongType()
            TokenType.STRING_KEYWORD -> ShortType()
            TokenType.INT_KEYWORD -> IntType()
            TokenType.DOUBLE_KEYWORD -> DoubleType()
            TokenType.BOOLEAN_KEYWORD -> BooleanType()
            TokenType.FLOAT_KEYWORD -> FloatType()
            TokenType.BYTE_KEYWORD -> ByteType()
            TokenType.SHORT_KEYWORD -> ShortType()
            TokenType.LONG_KEYWORD -> LongType()
            TokenType.UBYTE_KEYWORD -> UByteType()
            TokenType.ULONG_KEYWORD -> ULongType()
            null -> throw Exception("invalid atomic type")
            else -> {
                throw Exception("invalid atomic type")
            }
        }
    }

    override fun toKotlinCode(env: SymbolTable): String {
        println("value is $value")
        return when (type) {
            TokenType.SYMBOL -> value
            TokenType.NUMBER -> value
            TokenType.NONE -> "Unit"
            TokenType.STRING -> value
            TokenType.BOOLEAN -> value
            TokenType.COLON -> value
            TokenType.EOF -> throw Exception("code exited with partial execution")
            TokenType.THIS -> "this."
            TokenType.INT -> value
            TokenType.FLOAT -> value
            TokenType.LONG -> value
            TokenType.DOUBLE -> value
            TokenType.OBJECT -> value
            TokenType.NULLABLE -> value
            TokenType.ARRAY_INT -> "Int[]"
            TokenType.ARRAY_DOUBLE -> "Double[]"
            TokenType.ARRAY_OBJECT -> "object[]"
            TokenType.SELF -> "self."
            TokenType.NULL -> value
            TokenType.BYTE -> value
            TokenType.SHORT -> value
            TokenType.UBYTE -> value
            TokenType.ULONG -> value
            TokenType.STRING_KEYWORD -> "String"
            TokenType.INT_KEYWORD -> "Int"
            TokenType.DOUBLE_KEYWORD -> "Double"
            TokenType.BOOLEAN_KEYWORD -> "Boolean"
            TokenType.FLOAT_KEYWORD -> "Float"
            TokenType.BYTE_KEYWORD -> "Byte"
            TokenType.SHORT_KEYWORD -> "Short"
            TokenType.LONG_KEYWORD -> "Long"
            TokenType.UBYTE_KEYWORD -> "UByte"
            TokenType.ULONG_KEYWORD -> "ULong"
            null -> throw Exception("invalid atomic type")
            else -> {
                throw Exception("invalid atomic type")
            }
        }
    }
}