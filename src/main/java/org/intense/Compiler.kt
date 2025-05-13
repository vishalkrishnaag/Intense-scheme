package org.intense;

import org.intense.Types.GenericType
import org.intense.ast.ASTNode
import java.io.File

class Compiler(private var typeStore: TypingTable,private var environment: SymbolTable) {

    fun generateKotlinFile(astNodes: List<ASTNode>, outputPath: String) {
        val codeBuilder = StringBuilder()
        val types = GenericType()

        for (node in astNodes) {
            println("evaluating $node")
            val code = node.toKotlinCode(typeStore, environment)
            codeBuilder.appendLine(code)
        }

        File(outputPath).writeText(codeBuilder.toString())
    }


}
