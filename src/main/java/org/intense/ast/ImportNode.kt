package org.intense.ast;

import org.intense.Env
import org.intense.TreeWalk
import org.intense.Lexer
import org.intense.Parser
import org.intense.Types.UnitVal
import org.intense.Types.Value
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.concurrent.ExecutionException

class ImportNode(value: String, castingVar: AtomNode?) : ASTNode() {
    private var dependency: String = value
    private var castedVar: AtomNode? = castingVar
    fun processDependency(dependency: String, env: Env) {
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
                        val interpreter = TreeWalk(env)
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


    override fun eval(env: Env): Value {
        val trimmed: String = if (dependency.startsWith("\"") && dependency.endsWith("\"") && dependency.length >= 2) {
            dependency.substring(1, dependency.length - 1)
        } else {
            dependency
        }
       return UnitVal()
    }
}
