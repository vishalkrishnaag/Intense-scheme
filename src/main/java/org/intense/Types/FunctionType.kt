package org.intense.Types

class FunctionType(arguments: MutableList<Type>, dataType:Type) : Type() {
    var paramTypes: List<Type>? = arguments
    var returnType: Type = dataType
}