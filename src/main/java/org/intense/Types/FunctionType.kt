package org.intense.Types

abstract class FunctionType : Type() {
    var paramTypes: List<Type>? = null
    var returnType: Type? = null
}