package org.intense.ast;

import org.intense.Env
import org.intense.Types.NumVal
import org.intense.Types.StrVal
import org.intense.Types.Value

class EnumNode(atom: AtomNode, dataListNode: MutableList<ASTNode>) : ASTNode() {
    private var body:MutableList<ASTNode>? = dataListNode
    private var name:AtomNode = atom


    override fun eval(env: Env): Value {
        val output = StringBuilder()
        if (body!!.isNotEmpty()) {
            output.append("(")
            for (it in body!!.indices)
            {

                if (it < body!!.lastIndex) {
                    if(body!![it] is AtomNode)
                    {
                        output.append("< " + body!![it] + ", "+it.toDouble()+"> ")
                        output.append(", ")
                        val atomic = body!![it].toString()
                        env.define(atomic, NumVal(it.toDouble()))
                    }
                    else{
                        throw Exception("only atomic values  allowed  in enum but received "+body!![it])
                    }

                }
            }
            output.append(")")
        }
        return StrVal(output.toString())

    }
}
