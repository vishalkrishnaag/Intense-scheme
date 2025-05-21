package org.intense.Symbols

import org.intense.Types.Type
import org.intense.ast.ASTNode

sealed class Symbol(mType:Type, private val _scope:Boolean = false) {
    // false means local scope ie inside a method
    // true means field

    var scope:Boolean = _scope
   var type:Type= mType
}

data class FunctionSymbol(
    val paramCount :Int,
    var mType:Type
) : Symbol(mType)

data class VarSymbol(val mType: Type,val _scope:Boolean=false) : Symbol(mType,_scope)
data class ValSymbol(val mType: Type,val _scope:Boolean=true) : Symbol(mType,_scope)

data class ObjectSymbol(
    val name: String,
    val className: String,
    val fieldValues: MutableMap<String, ASTNode>,
    val mType: Type
) : Symbol(mType)

data class ClassSymbol(
    val vName: String,
    val vSuperclass: ClassSymbol?, // or ClassSymbol?
    val vFields: Map<String, Type>?,
    val vMethods: Map<String, FunctionSymbol>?,
    val mType: Type
) : Symbol(mType) {
    private val name: String = vName
    private val superclass: ClassSymbol? = vSuperclass
    private val fields: Map<String, Type>? = vFields
    private val methods: Map<String, FunctionSymbol>? = vMethods
}


