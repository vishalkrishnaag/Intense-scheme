package org.intense.ast;

import org.intense.SymbolTable;
import org.intense.Types.Type

class CallNode(private var operand: ASTNode?, private var is_std: Boolean, private var params: List<ASTNode>?) :
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
            true
        } else {
            false
        }
//        var mType:Type = type.lookup(env.lookup(operand.toString()).type)


        if (is_std) {
            if (params != null) {
                if (params!!.isNotEmpty()) {
                    val oper_code = StringBuilder()
                    oper_code.append(params!![0].toKotlinCode(env))
                    oper_code.append(
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
                                throw Exception("Invalid operator detected $code")
                            }
                        }
                    )

                    for (it in 1..<params!!.size) {
                        oper_code.append(params!![it].toKotlinCode(env))
                    }
                    println("call Node is $oper_code")
                    return oper_code.toString()
                } else {
                    throw Exception("std methods requires params")
                }

            }

        }
        val callCode = StringBuilder()
        if (params!!.isNotEmpty()) {
            for (it in params!!.indices) {
                callCode.append(params!![it].toKotlinCode(env))
                if (it < params!!.lastIndex) {
                    callCode.append(", ")
                }
            }

            if (annotated) {
                return "\n$code \n$callCode"
            } else {
                return "\n$code($callCode)"
            }


        } else {
            if (annotated) {
                return "\n$code\n"
            } else {
                return "\n$code()"
            }

        }

    }

}
