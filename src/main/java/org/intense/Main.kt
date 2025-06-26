package org.intense;

import java.io.File
import java.nio.file.Files;
import java.nio.file.Paths;
import kotlin.io.nameWithoutExtension
import kotlin.io.readText

    fun main(args: Array<String>) {
        if (args.isNotEmpty()) {
            // File mode
            try {
                val path = Paths.get(args[0])
                val content = Files.readString(path)

                val lexer = Lexer(content)
                val environment = SymbolTable(null)
                val parser = Parser(lexer,environment)
                val astNodes = parser.parseTree
                val treeWalk = TreeWalk(environment)
                val newFile = File(
                    path.parent.toFile(),
                    path.fileName.toString().substringBeforeLast('.') + ".kt"
                )
                treeWalk.generateKotlinFile(astNodes,newFile.path)

            } catch (e: Exception) {
                System.err.println("Error: ${e.message}")
            }
        } else {
            throw Exception("Input file is required")
        }
    }




