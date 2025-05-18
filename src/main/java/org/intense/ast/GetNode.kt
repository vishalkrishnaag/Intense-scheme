package org.intense.ast;

import org.intense.SymbolTable;
import org.intense.Symbols.Symbol
import org.intense.Types.GenericType
import org.intense.Types.Type

class GetNode(input:ASTNode) : ASTNode() {
    private var body:ASTNode = input

    override fun inferType(env: SymbolTable): Type {
        return GenericType()
    }

    override fun toKotlinCode(env: SymbolTable): String {
           // Todo: Need checking and conversion to methods and fields
           println("$body is type ")
          return  "\n" + body.toKotlinCode(env) + "\n"
    }
}
