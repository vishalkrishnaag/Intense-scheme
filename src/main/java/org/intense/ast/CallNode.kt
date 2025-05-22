package org.intense.ast;

import org.intense.SymbolTable
import org.intense.Symbols.BuiltInMethodSymbol
import org.intense.Symbols.FunctionSymbol
import org.intense.Types.Type

class CallNode(private var operand: ASTNode?, private var params: List<ASTNode>?) :
    ASTNode() {

    override fun inferType(env: SymbolTable): Type {
        TODO("Not yet implemented")
    }

    override fun toKotlinCode(env: SymbolTable): String {
        var code: String = operand?.toKotlinCode(env) ?: "no_body"
        var argumentRequired: Boolean = true
        // todo: !code.contains(".") remove this code
        if (code.contains('.')) {
            if(env.lookup(code) is FunctionSymbol)
            {
                val func: FunctionSymbol = env.lookup(code) as FunctionSymbol
                if (func.paramCount == 0) {
                    argumentRequired = false
                }
                if (params != null) {
                    if (params!!.size != func.paramCount) {
                        throw Exception("Argument Count Miss match : method $code expects ${func.paramCount} of arguments but user provided ${params!!.size} arguments")
                    }
                }
                if(params == null && argumentRequired)
                {
                    throw Exception("method $code expects ${func.paramCount} of arguments but user provided no arguments")
                }
            }
        }

        if (env.lookup(code) is BuiltInMethodSymbol) {
           // do something
        }
        val annotated = if (code[0] == '@') {
            if (code.contains("-")) {
                code = "@in10s_" + code.replace("-", "_")
                    .replace("@", "")
            }
            true
        } else {
            false
        }

        val callCode = StringBuilder()
        if (params!!.isNotEmpty()) {

            if (code in listOf("add", "sub", "mul", "div", "mod", "pow", "greater", "lesser", "not")) {
                callCode.append(params!![0].toKotlinCode(env))
                callCode.append(
                    when (code) {
                        "add" -> {
                            " + "
                        }

                        "sub" -> {
                            " - "
                        }

                        "mul" -> {
                            " * "
                        }

                        "div" -> {
                            " / "
                        }

                        "mod" -> {
                            " % "
                        }

                        "pow" -> {
                            " ^ "
                        }

                        "greater" -> {
                            " > "
                        }

                        "lesser" -> {
                            " < "
                        }

                        "not" -> {
                            " ! "
                        }

                        else -> {
                            code
                        }
                    }
                )
                for (it in 1..<params!!.size) {
                    callCode.append(params!![it].toKotlinCode(env))
                }
                return "$callCode"
            } else {
                for (it in params!!.indices) {
                    callCode.append(params!![it].toKotlinCode(env))
                    if (it < params!!.lastIndex) {
                        callCode.append(", ")
                    }
                }
            }


            return if (annotated) {
                "\n$code \n$callCode"
            } else {
                "\n$code($callCode)"
            }


        } else {
            return if (annotated) {
                "\n$code\n"
            } else {
                "\n$code()"
            }

        }

    }

}
