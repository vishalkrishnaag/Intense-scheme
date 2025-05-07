package org.intense.ast

abstract class FunctionType : Type() {
    var paramTypes: List<Type>? = null
    var returnType: Type? = null
}