package org.intense.ast;

import org.intense.SymbolTable;
import org.intense.Types.Type
import org.intense.TypingTable

class CallNode(private var operand: ASTNode?, private var is_std: Boolean, private var params: List<ASTNode>?) :
    ASTNode() {

    override fun inferType(type: TypingTable, env: SymbolTable): Type {
        TODO("Not yet implemented")
    }

    override fun toKotlinCode(type: TypingTable, env: SymbolTable): String {
        val code: String = operand?.toKotlinCode(type, env) ?: "no_body"
//        var mType:Type = type.lookup(env.lookup(operand.toString()).typeId)
        

        if (is_std) {
            if (params != null) {
                if (params!!.isNotEmpty()) {
                    val oper_code = StringBuilder()
                    oper_code.append(params!![0].toKotlinCode(type, env))
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
                        oper_code.append(params!![it].toKotlinCode(type, env))
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
            for (it in params!!.indices)
            {
                callCode.append(params!![it].toKotlinCode(type, env))
                if (it < params!!.lastIndex) {
                    callCode.append(", ")
                }
            }

            return "\n$code($callCode)"
        } else {
            return "\n$code()"
        }

    }

}
