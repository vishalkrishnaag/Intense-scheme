package org.intense.ast;

import org.intense.Env;
import org.intense.Types.MapVal;
import org.intense.Types.Pair;
import org.intense.Types.Value;

import java.util.ArrayList;

public class MapNode extends ASTNode {
    private ArrayList<Pair<ASTNode,ASTNode>> keyMap =new ArrayList<>();

    public MapNode(ArrayList<Pair<ASTNode, ASTNode>> keyMap) {
        this.keyMap = keyMap;
    }

    @Override
    public Value eval(Env env) {
        MapVal map = new MapVal();
        for (var entry : keyMap) {
            Value key = entry.getKey().eval(env);
            Value val = entry.getValue().eval(env);
            map.value.put(key, val);
        }
        return map;
    }
}
