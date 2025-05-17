package org.intense.ast

import org.intense.Types.Type

sealed class Symbol(mTypeId: Int) {
   var typeId:Int= mTypeId
}

data class FunctionSymbol(
    val paramCount :Int,
    var mTypeId:Int
) : Symbol(mTypeId)

data class VarSymbol(val mTypeId: Int) : Symbol(mTypeId)
data class ValSymbol(val mTypeId: Int) : Symbol(mTypeId)

data class ObjectSymbol(
    val name: String,
    val className: String,
    val fieldValues: MutableMap<String, ASTNode>,
    val mTypeId: Int
) : Symbol(mTypeId)

data class ClassSymbol(
    val name: String,
    val superclass: ClassSymbol?, // or ClassSymbol?
    val fields: Map<String, Type>,
    val methods: Map<String, FunctionSymbol>,
    val mTypeId: Int
) : Symbol(mTypeId)


