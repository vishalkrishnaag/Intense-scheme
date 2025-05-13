package org.intense.ast;

import org.intense.SymbolTable;
import org.intense.Types.FunctionType
import org.intense.Types.Type
import org.intense.TypingTable

class DefNode(
    atom: AtomNode,
    defDataType: ASTNode,
    defArguments: MutableList<ASTNode>? = null,
    dataListNode: MutableList<ASTNode>
) : ASTNode() {
    private var body: MutableList<ASTNode> = dataListNode
    private var name: AtomNode = atom
    private var dataType = defDataType
    private var arguments:List<ASTNode>? = defArguments

    override fun inferType(type: TypingTable, env: SymbolTable): Type {
        val argTypes = mutableListOf<Type>()
        if(arguments!=null&&arguments!!.isNotEmpty()) {
            for (it in arguments!!) {
                argTypes.add(it.inferType(type, env))
            }
        }
        return FunctionType(argTypes,dataType.inferType(type, env))
    }

    override fun toKotlinCode(type: TypingTable, env: SymbolTable): String {
        val code = StringBuilder()
        val index : Int = type.define(this.inferType(type,env))
        if(arguments!=null)
        {
            env.define(name.value,FunctionSymbol(arguments!!.size,index))
        }
        else{
            env.define(name.value,FunctionSymbol(0,index))
        }


        code.append("fun " + name.value)
        if (arguments !=null && arguments?.isNotEmpty() == true) {
            val args: StringBuilder = StringBuilder()
            for (it in arguments?.indices!!) {
                args.append(arguments!![it].toKotlinCode(type, env))
                if (it < arguments!!.lastIndex) {
                    args.append(", ")
                }

            }
            code.append("(${args}) :" + dataType.toKotlinCode(type, env) + "{\n\t")
        } else {
            code.append("() :" + dataType.toKotlinCode(type, env) + "{\n\t")
        }

        for (it in body) {
            code.append(it.toKotlinCode(type, env))
        }
        code.append("\n}")
        return code.toString()
    }
}
