package org.intense.ast

import org.intense.SymbolTable
import org.intense.Types.Type

class TryCatchNode(mTryBlock: ASTNode) :ASTNode() {
    private var tryBlock: ASTNode = mTryBlock
    override fun inferType(env: SymbolTable): Type {
        TODO("Not yet implemented")
    }

    override fun toKotlinCode(env: SymbolTable): String {
        try {

        }catch (ex:Exception) {

        }
        return "try {${tryBlock.toKotlinCode(env)} "
    }
}