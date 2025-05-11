package org.intense.ast;

import org.intense.SymbolTable;
import org.intense.Types.IntType
import org.intense.Types.Type

class DefNode(
    atom: AtomNode,
    defDataType: ASTNode,
    defArguments: MutableList<ASTNode>,
    dataListNode: MutableList<ASTNode>
) : ASTNode() {
    private var body: MutableList<ASTNode> = dataListNode
    private var name: AtomNode? = atom
    private var dataType = defDataType
    private var arguments = defArguments

    override fun inferType(env: SymbolTable): Type {
        return IntType()
    }

    override fun toKotlinCode(env: SymbolTable): String {
        val code = StringBuilder()

        code.append("fun " + name?.value)
        if (arguments.isNotEmpty()) {
            val args: StringBuilder = StringBuilder()
            for (it in arguments.indices) {
                args.append(arguments[it].toKotlinCode(env))
                if (it < arguments.lastIndex) {
                    args.append(", ")
                }

            }
            code.append("(${args}) :" + dataType.toKotlinCode(env) + "{\n\t")
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
