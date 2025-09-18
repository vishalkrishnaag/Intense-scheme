package org.intense;
import org.intense.Types.NullVal;
import org.intense.Types.Value;
import org.intense.ast.ASTNode;

import java.util.List;

public class TreeWalk {

    public TreeWalk(Env environment) {
    }

    public Value traverse(List<ASTNode> astNodes, Env env) {
        Value last = NullVal.INSTANCE; // Assuming UnitVal is a singleton with INSTANCE field
        for (ASTNode node : astNodes) {
            System.out.println("evaluating " + node);
            last = node.eval(env);
            System.out.println("=> " + last);
        }
        return last;
    }
}

