package org.intense;

import org.intense.ast.ASTNode;
import org.intense.ast.AtomNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Builtin {

    static int size =0;
    static AtomNode atom;
    static ASTNode first;
    static List<ASTNode> elements;
    static String operation;

    public Builtin(String mOperation,int mSize,ASTNode mFirst,AtomNode mAtom, List<ASTNode> body) {
        size = mSize;
        atom = mAtom;
        first = mFirst;
        elements = body;
        operation = mOperation;
    }



    @FunctionalInterface
    interface BuiltInFunction {
        Object apply();
    }

    private final Map<String, BuiltInFunction> builtInMethods = new HashMap<>();

    public static Object call() {
        return null;
    }
}
