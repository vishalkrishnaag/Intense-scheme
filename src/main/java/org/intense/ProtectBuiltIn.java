package org.intense;
import java.util.Set;

public final class ProtectBuiltIn {
    Set<String> protectedSymbols = Set.of(
            "+",
            "-",
            "*",
            "/",
            "%",
            "<",
            ">",
            "!=",
            "addSymbol?",
            "sub?",
            "is-null?",
            "less-than?",
            "greater-than?",
            "less-or-equal?",
            "greater-or-equal?",
            "define",
            "equals?",
            "not-equals?",
            "not-of",
            "make-instance!");

    public ProtectBuiltIn(String name) {
        System.out.println("verified ....");
        if (protectedSymbols.contains(name)) {
            throw new RuntimeException("Cannot redefine built-in: " + name);
        }
    }
}
