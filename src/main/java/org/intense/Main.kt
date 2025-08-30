package org.intense;

import java.io.File
import java.nio.file.Files;
import java.nio.file.Paths;
import kotlin.io.nameWithoutExtension
import kotlin.io.readText

    fun main(args: Array<String>) {
        val environment = Env(null)
        if (args.isNotEmpty()) {
            // File mode
            try {
                val path = Paths.get(args[0])
                val content = Files.readString(path)

                val lexer = Lexer(content)
                val parser = Parser(lexer)
                val astNodes = parser.parseTree
                val treeWalk = TreeWalk(environment)
                treeWalk.traverse(astNodes,environment)

            } catch (e: Exception) {
                System.err.println("Error: ${e.message}")
            }
        } else {
            // REPL mode
            println("Welcome to INTENSE REPL. Type 'exit' to quit.")
            val treeWalk = TreeWalk(environment)

            while (true) {
                print(">>> ")
                val line = readLine() ?: break
                if (line.trim().lowercase() == "exit") break

                try {
                    val lexer = Lexer(line)
                    val parser = Parser(lexer)
                    val astNodes = parser.parseTree

                    treeWalk.traverse(astNodes, environment)
                } catch (e: Exception) {
                    println("Error: ${e.message}")
                }
            }
        }
    }




