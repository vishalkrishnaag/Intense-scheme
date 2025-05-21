package org.intense.ast;

import org.intense.SymbolTable;
import org.intense.Types.Type

class LinkingNode(instanceValue: String, element: MutableList<String>) : ASTNode(){
    var instanceName = instanceValue;
    var elements: List<String> = element
    /**
     * .b.c tokenType is linking Node
     * primary functions are object life cycles
     * */

    override fun inferType(env: SymbolTable): Type {
        TODO("Not yet implemented")
    }

    override fun toKotlinCode(env: SymbolTable): String {
        val output = StringBuilder()
        if (elements.isNotEmpty()) {
            for (it in elements)
            {
                //todo: add validation for class.objects
                output.append(it)
            }
        }
        return "$instanceName$output"
    }
}
