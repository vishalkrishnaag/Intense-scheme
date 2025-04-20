package org.intense.Ast;

import java.util.List;

public class MapNode extends ASTNode{
    public final List<ASTNode> object;

    public MapNode(List<ASTNode> object) {
        this.object = object;
    }

    @Override
    public Object eval()  {
        return new Exception("this Section is under development");
    }
}
