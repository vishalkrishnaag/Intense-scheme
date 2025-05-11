package org.intense;

import org.intense.ast.ASTNode
import java.io.File

class Compiler(private var environment: SymbolTable) {

    fun generateKotlinFile(astNodes: List<ASTNode>, outputPath: String) {
        val codeBuilder = StringBuilder()

        for (node in astNodes) {
            println("evaluating $node")
            val code = node.toKotlinCode(environment)
            codeBuilder.appendLine(code)
        }

        File(outputPath).writeText(codeBuilder.toString())
    }


}
