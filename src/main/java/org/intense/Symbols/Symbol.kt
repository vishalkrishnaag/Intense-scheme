package org.intense.Symbols

import org.intense.Types.Type
import org.intense.ast.ASTNode

sealed class Symbol(mTypeId: Int, _scope:Boolean = false) {
    // false means local scope ie inside a method
    // true means field
    var scope:Boolean = _scope
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
    val vName: String,
    val vSuperclass: ClassSymbol?, // or ClassSymbol?
    val vFields: Map<String, Type>,
    val vMethods: Map<String, FunctionSymbol>,
    val mTypeId: Int
) : Symbol(mTypeId) {
    private  val name: String = vName
    private  val superclass: ClassSymbol? = vSuperclass
    private  val fields: Map<String, Type> = vFields
    private  val methods: Map<String, FunctionSymbol> = vMethods
}


