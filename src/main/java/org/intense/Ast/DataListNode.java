package org.intense.Ast;

import java.util.List;

public class DataListNode extends ASTNode{
    private final List<ASTNode> elements;

    public DataListNode(List<ASTNode> elements) {
        this.elements = elements;
    }

    @Override
    public Object eval() {
        return new Exception("This section Not Implemented");
    }
}
