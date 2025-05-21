package org.intense;

import com.facebook.ktfmt.format.Formatter
import org.intense.ast.ASTNode
import java.io.File

class Compiler(private var environment: SymbolTable) {
    private fun formatKotlinCode(code: String): String {
        return Formatter.format(code)
    }


    fun generateKotlinFile(astNodes: List<ASTNode>, outputPath: String) {
        val codeBuilder = StringBuilder()

        for (node in astNodes) {
            println("evaluating $node")
            val code = node.toKotlinCode(environment)
            codeBuilder.appendLine(code)
        }

        File(outputPath).writeText(formatKotlinCode(codeBuilder.toString()))
//        File(outputPath).writeText(codeBuilder.toString())
    }


}
