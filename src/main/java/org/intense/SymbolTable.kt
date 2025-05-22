package org.intense;

import org.intense.Symbols.BuiltInMethodSymbol
import org.intense.Symbols.Symbol
import org.intense.Types.GenericType


class SymbolTable(private val parent: SymbolTable? = null) {
    private val symbols: MutableMap<String, Symbol> = mutableMapOf()

    fun define(name: String, symbol: Symbol) {
        symbols[name] = symbol
    }
    fun defineV(name: String, symbol: Symbol) {
        symbols[name] = symbol
    }

    fun lookup(name: String): Symbol {
        if(name in listOf("println","add", "sub", "mul", "div", "mod", "pow", "greater", "lesser", "not"))
        {
            // Change generic type to some other types like built in method Type
            return BuiltInMethodSymbol(0,GenericType())
        }
        if(name.contains('.'))
        {
            val parts = name.split(".")
            var lastLookup:Symbol? = null
            for ((index, part) in parts.withIndex()) {
                lastLookup = symbols[part]
                println("Part $index: $part lookup:${symbols[part]}")
            }
            if(lastLookup!=null)
            {
                return lastLookup
            }
        }
        return symbols[name] ?: parent?.lookup(name)
        ?: throw Exception("Symbol '$name' not found")
    }

    fun update(name: String, symbol: Symbol) {
        if (symbols.containsKey(name)) {
            symbols[name] = symbol
        } else {
            parent?.update(name, symbol) ?: throw Exception("Cannot update undefined symbol '$name'")
        }
    }
}

