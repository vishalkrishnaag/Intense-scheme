package org.intense;
import org.intense.ast.ASTNode;
import org.intense.ast.Compiler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;

fun main(args: Array<String>) {
    if (args.isNotEmpty()) {
        // File mode
        try {
            val path = Paths.get(args[0])
            val content = Files.readString(path)

            val lexer = Lexer(content)
            val parser = Parser(lexer)
            val astNodes = parser.parseTree

            val environment = SymbolTable(null)
            val compiler = Compiler(environment)

            compiler.generateKotlinFile(astNodes, content)

        } catch (e: Exception) {
            System.err.println("Error: ${e.message}")
        }
    } else {
        throw Exception("Input file is required")
    }
}



