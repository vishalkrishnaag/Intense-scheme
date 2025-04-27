package org.intense.Ast;

import org.intense.Environment;
import org.intense.TokenType;

import java.util.HashMap;
import java.util.Map;

public class MapNode extends ASTNode {
    Map<String,ASTNode> object = new HashMap<>();

    public MapNode(Map<String,ASTNode> object) {
        this.object = object;
    }

    @Override
    public Object eval(Environment env) {
//        env.define(object.keySet().toString(),object.values().stream().toList());
        return object;
    }
}
