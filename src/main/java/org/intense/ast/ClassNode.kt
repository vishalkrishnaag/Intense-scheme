package org.intense.ast;

import org.intense.Env
import org.intense.Types.BuiltinClsVal
import org.intense.Types.ClVal
import org.intense.Types.Value

class ClassNode(atom: AtomNode, dataListNode: MutableList<ASTNode>,extends: MutableList<ASTNode>?) : ASTNode() {
    private var body:MutableList<ASTNode>? = dataListNode
    private var name:AtomNode = atom
    private var extend: MutableList<ASTNode>?= extends

    override fun eval(env: Env): Value {
        val output = StringBuilder()
        val extendedClasses = StringBuilder()
        if(extend!=null)
        {
            for (it in extend!!.indices) {
                extendedClasses.append(extend!![it].eval(env))
                if (it < extend!!.lastIndex) {
                    extendedClasses.append(", ")
                }
            }
        }

        if (body!!.isNotEmpty()) {
            for (it in body!!)
            {
                output.append(it.eval(env))
            }
        }
        if(extendedClasses.isNotEmpty())
        {
            return BuiltinClsVal()
        }
        else{
            return ClVal()
        }


    }
}
