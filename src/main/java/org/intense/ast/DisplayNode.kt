package org.intense.ast;

import org.intense.SymbolTable;

class DisplayNode : ASTNode() {
    private var printable: ASTNode? = null;
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
        TODO("Not yet implemented")
    }

    override fun eval(env: SymbolTable): String {
        TODO("Not yet implemented")
    }
}
