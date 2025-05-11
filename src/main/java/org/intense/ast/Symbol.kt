package org.intense.ast

import org.intense.Types.Type

sealed class Symbol

data class VariableSymbol(val name: String, val type: Type) : Symbol()

data class FunctionSymbol(
    val name: String,
    val paramTypes: List<Type>,
    val returnType: Type
) : Symbol()

data class ObjectSymbol(
    val name: String,
    val className: String,
    val fieldValues: MutableMap<String, ASTNode>
) : Symbol()

data class ClassSymbol(
    val name: String,
    val superclass: ClassSymbol?, // or ClassSymbol?
    val fields: Map<String, Type>,
    val methods: Map<String, FunctionSymbol>
) : Symbol()


