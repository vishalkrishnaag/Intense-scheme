package org.intense.ast;

import org.intense.SymbolTable;
import org.intense.Types.Type
import org.intense.TypingTable

class DisplayNode(mPrintable:ASTNode) : ASTNode() {
    private var printable: ASTNode? = mPrintable
    fun getPrintable() : ASTNode? {
        return printable;
    }

    fun setPrintable(printable: ASTNode) {
        this.printable = printable;
    }

    override fun inferType(type: TypingTable, env: SymbolTable): Type {
        TODO("Not yet implemented")
    }

    override fun toKotlinCode(type: TypingTable, env: SymbolTable): String {
        return "println "+printable?.toKotlinCode(type, env)
    }
}
