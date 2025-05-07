package org.intense.ast;

import org.intense.SymbolTable;
import java.io.File
import java.io.BufferedWriter
import java.io.FileWriter
import java.util.concurrent.*;

class Compiler(environment: SymbolTable) {
   private lateinit var environment:SymbolTable

    fun generateKotlinFile(astNodes: List<ASTNode>,outputPath: String) {
        val codeBuilder = StringBuilder()

        for (node in astNodes) {
            val code = node.toKotlinCode(environment)
            codeBuilder.appendLine(code)
        }

        File(outputPath).writeText(codeBuilder.toString())
    }


}
