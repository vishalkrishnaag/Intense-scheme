package org.intense.ast;

import org.intense.SymbolTable;
import org.intense.Types.Type

class DisplayNode(mPrintable:ASTNode) : ASTNode() {
    private var printable: ASTNode? = mPrintable
    fun getPrintable() : ASTNode? {
        return printable;
    }

    fun setPrintable(printable: ASTNode) {
        this.printable = printable;
    }

    override fun inferType(env: SymbolTable): Type {
        TODO("Not yet implemented")
    }

    override fun toKotlinCode(env: SymbolTable): String {
        return "println "+printable?.toKotlinCode(env)
    }
}
