package org.intense;

import org.intense.ast.Symbol


class SymbolTable(val parent: SymbolTable? = null) {
    private val symbols: MutableMap<String, Symbol> = mutableMapOf()

    fun define(name: String, symbol: Symbol) {
        symbols[name] = symbol
    }

    fun lookup(name: String): Symbol {
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

