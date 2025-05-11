package org.intense.ast;

import org.intense.Compiler
import org.intense.Lexer
import org.intense.Parser
import org.intense.SymbolTable;
import org.intense.Types.Type
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.concurrent.ExecutionException

class ImportNode(value: String) : ASTNode() {
   lateinit var dependency: String
    fun processDependency(dependency: String, env: SymbolTable) {
        // Step 1: Convert string to Path
        val path: Path = Paths.get(dependency)

        // Step 2: Convert Path to File and check existence
        val file = path.toFile()

        if (file.exists()) {
            try {
                when {
                    file.isDirectory -> {
                        println("The path is a directory: ${file.absolutePath}")
                    }
                    file.isFile -> {
                        println("executing file: ${file.absolutePath}")
                        val content = Files.readString(file.toPath())
                        val lexer = Lexer(content)
                        val parser = Parser(lexer)
                        val astNodes = parser.parseTree // assuming Kotlin-style property
                        val environment = SymbolTable(env)
                        val interpreter = Compiler(environment)
                        TODO("need completion")
                    }
                    else -> {
                        println("The path exists, but it's neither a file nor a directory.")
                    }
                }
            } catch (e: Exception) {
                when (e) {
                    is ExecutionException,
                    is InterruptedException,
                    is IOException -> {
                        System.err.println("Failed to read file: ${e.message}")
                        throw RuntimeException(e)
                    }
                    else -> throw e
                }
            }
        } else {
            println("The path does not exist: ${file.absolutePath}")
        }
    }
    override fun inferType(env: SymbolTable): Type {
        TODO("Not yet implemented")
    }

    override fun toKotlinCode(env: SymbolTable): String {
        TODO("Not yet implemented")
    }
}
