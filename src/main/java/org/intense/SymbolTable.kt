package org.intense;

import org.intense.Types.Type
import org.intense.Symbols.Symbol


class SymbolTable(private val parent: SymbolTable? = null) {
    private val symbols: MutableMap<String, Symbol> = mutableMapOf()
    private val typeStore:TypingTable = TypingTable()
    fun getTypeStore(): TypingTable {
        return this.typeStore
    }

    fun define(name: String, symbol: Symbol) {
        symbols[name] = symbol
    }
    fun defineV(name: String, symbol: Symbol, varType: Type) {
       val typeId:Int = typeStore.define(varType)
        symbol.typeId = typeId
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

