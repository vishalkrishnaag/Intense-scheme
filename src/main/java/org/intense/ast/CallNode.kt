package org.intense.ast;

import org.intense.SymbolTable;
import org.intense.Types.Type

class CallNode(private var operand: ASTNode?, private var params: List<ASTNode>?) :
    ASTNode() {

    override fun inferType(env: SymbolTable): Type {
        TODO("Not yet implemented")
    }

    override fun toKotlinCode(env: SymbolTable): String {
        var code: String = operand?.toKotlinCode(env) ?: "no_body"
        val annotated = if (code[0] == '@') {
            if (code.contains("-")) {
                code = "@in10s_" + code.replace("-", "_")
                    .replace("@","")
            }
            true } else { false }

        val callCode = StringBuilder()
        if (params!!.isNotEmpty()) {
            if(code in listOf("add","sub","mul","div","mod","pow","greater","lesser","not"))
            {
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
            }
            else{
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
