package org.intense.Ast;

import org.intense.Environment;

import java.util.Map;

public class IfConditionNode extends ASTNode {
    private final ASTNode if_exp;
    private final ASTNode if_body;
    private final ASTNode else_body;

    public IfConditionNode(ASTNode if_exp, ASTNode if_body, ASTNode else_body) {
        this.else_body = else_body;
        this.if_body = if_body;
        this.if_exp = if_exp;
    }


    @Override
    public Object eval(Environment env) {
        Object expr = if_exp.eval(env);
        if (expr instanceof Boolean result) {
            if (if_body == null || if_exp == null)
                throw new RuntimeException("Expected statements in if condition");

            return result ? if_body.eval(env) : else_body.eval(env);
        }
        return null;
    }
}
