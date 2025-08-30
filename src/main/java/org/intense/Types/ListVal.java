package org.intense.Types;

import org.intense.ast.ASTNode;

import java.util.List;

public class ListVal extends Value {
    public final List<ASTNode> value;

    public ListVal(List<ASTNode> value) {
        this.value = value;
    }
    @Override public String asString() { return value.toString(); }

    @Override
    public String toString() {
        return "\"" + value + "\"";
    }
}
