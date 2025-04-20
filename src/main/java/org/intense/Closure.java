package org.intense;

import org.intense.Ast.ASTNode;

import java.util.List;
import java.util.Optional;

public class Closure {

    private final List<ASTNode> body;

    public Closure(List<ASTNode> body) {
        this.body = body;
    }

    public Object apply(List<Object> args) {

        try {
            Object result = null;
            //1 st is lambda
            for (ASTNode astNode : body) {
                result = astNode.eval();
            }
            return Optional.ofNullable(result);
        } catch (Exception e) {
            e.printStackTrace();  // Log it, don't crash thread
            return Optional.empty();
        }
    }
    @Override
    public String toString() {
        return "(closure)";
    }
}
