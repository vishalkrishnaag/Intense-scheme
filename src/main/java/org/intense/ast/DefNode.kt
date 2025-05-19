package org.intense.ast;

import org.intense.SymbolTable;
import org.intense.Types.FunctionType
import org.intense.Types.Type

class DefNode(
    atom: AtomNode,
    returnType: ASTNode,
    defArguments: MutableList<DefArgumentNode>? = null,
    dataListNode: MutableList<ASTNode>,
    questions : Boolean
) : ASTNode() {
    private var body: MutableList<ASTNode> = dataListNode
    private var name: AtomNode = atom
    private var dataType = returnType
    private var arguments:List<DefArgumentNode>? = defArguments
    private var question:Boolean = questions

    override fun inferType(env: SymbolTable): Type {
        val argTypes = mutableListOf<Type>()
        if(arguments!=null&&arguments!!.isNotEmpty()) {
            for (it in arguments!!) {
                argTypes.add(it.inferType(env))
            }
        }
        return FunctionType(argTypes,dataType.inferType(env))
    }

    override fun toKotlinCode(env: SymbolTable): String {
        val code = StringBuilder()

        code.append("fun " + name.value)
        if (arguments !=null && arguments?.isNotEmpty() == true) {
            val args: StringBuilder = StringBuilder()
            for (it in arguments?.indices!!) {
                args.append(arguments!![it].toKotlinCode(env))
                if (it < arguments!!.lastIndex) {
                    args.append(", ")
                }

            }
            if(question)
            {
                code.append("(${args}) :" + dataType.toKotlinCode(env) + "? {\n\t")
            }
                else{
                code.append("(${args}) :" + dataType.toKotlinCode(env) + "{\n\t")
            }

        } else {
            code.append("() :" + dataType.toKotlinCode(env) + "{\n\t")
        }

        for (it in body) {
            code.append(it.toKotlinCode(env))
        }
        code.append("\n}")
        return code.toString()
    }
}
