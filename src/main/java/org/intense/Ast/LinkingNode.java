package org.intense.Ast;

import org.intense.Environment;

import java.util.List;
import java.util.Map;

public class LinkingNode extends ASTNode{
    private final List<ASTNode> elements;
    /**
     * .b.c type is linking Node
     * primary functions are object life cycles
     * */
    public LinkingNode(List<ASTNode> elements) {
        this.elements = elements;
    }

    @Override
    public Object eval(Environment env) {
        return null;
    }
}
