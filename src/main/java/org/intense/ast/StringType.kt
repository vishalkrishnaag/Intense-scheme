package org.intense.ast;

import com.sun.org.apache.xalan.internal.xsltc.compiler.util.StringType



class StringType : Type() {

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    fun toKotlinType(): String {
        return "String"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return true
    }


}
