package org.intense.Ast;

import org.intense.Environment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DefNode extends ASTNode{
    private final List<ASTNode> body;
    private final AtomNode name;
    public DefNode(AtomNode methodName, List<ASTNode> body) {
        this.name = methodName;
        this.body = body;
    }

    @Override
    public Object eval(Environment env) {
        // List<ASTNode> function = new ArrayList<>(elements.subList(1, elements.size()));
         env.define(name.value,body);
        return "(closure)";
    }
}
