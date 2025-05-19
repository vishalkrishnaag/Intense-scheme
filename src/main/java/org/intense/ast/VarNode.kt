package org.intense.ast;

import org.intense.SymbolTable;
import org.intense.Symbols.Symbol
import org.intense.Types.Type

class VarNode(
    atom: AtomNode,
    mDataType: ASTNode?,
    dataListNode: DataListNode,
    question: Boolean
) : ASTNode() {
    private var body: DataListNode? = dataListNode
    private var name: AtomNode = atom
    private var questionMark: Boolean = question
    private var dataType: ASTNode? = mDataType

    override fun inferType(env: SymbolTable): Type {
        val atomic: Symbol = env.lookup(name.value)
        return atomic.type
    }

    override fun toKotlinCode(env: SymbolTable): String {
        if(dataType ==null)
        {
         return "\nvar "+name.value + " = "+ body?.toKotlinCode(env) + "()\n"
        }
        else{
            return if (questionMark) {
                if (dataType == null) {
                    throw Exception("invalid syntax ? is added without any data type")
                }
                "\nvar " + name.value + ":" + dataType?.toKotlinCode(env) + "? = " + body?.toKotlinCode(env) + "\n"
            } else {
                if (dataType == null) {
                    "\nvar " + name.value +  " = " + body?.toKotlinCode(env) + "\n"
                }else{
                    "\nvar " + name.value + ":" + dataType?.toKotlinCode(env) + " = " + body?.toKotlinCode(env) + "\n"
                }

            }
        }
    }
}
