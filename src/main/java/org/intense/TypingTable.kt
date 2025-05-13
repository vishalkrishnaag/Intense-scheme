package org.intense;

import org.intense.Types.Type


class TypingTable(private val parent: TypingTable? = null) {
    private val store= mutableListOf<Type>()

    fun define(type: Type):Int {
        store+= type
        return store.size
    }

    fun lookup(location:Int): Type {
        return store[ location] ?: parent?.lookup(location)
        ?: throw Exception("Invalid Look up , not found")
    }

    fun update(location:Int, updateType:Type) {
        if (location < store.size) {
            store[location] = updateType
        } else {
            parent?.update(location, updateType) ?: throw Exception("Cannot update undefined symbol ")
        }
    }
}

