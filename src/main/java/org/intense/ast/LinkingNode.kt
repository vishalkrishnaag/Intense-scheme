package org.intense.ast;

import org.intense.Env
import org.intense.Types.StrVal
import org.intense.Types.Value

class LinkingNode(instanceValue: String, element: MutableList<ASTNode>) : ASTNode(){
    private var instanceName = instanceValue;
    private var elements: List<ASTNode> = element
    /**
     * .b.c tokenType is linking Node
     * primary functions are object life cycles
     * */

    override fun eval(env: Env): Value {
        val output = StringBuilder()
        if (elements.isNotEmpty()) {
            for (it in elements)
            {
                //todo: add validation for class.objects
                output.append(it)
            }
        }
        return StrVal("$instanceName.$output")
    }
}
