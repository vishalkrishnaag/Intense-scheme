package org.intense.Ast;

import org.intense.Environment;

public class SetNode extends ASTNode{
    AtomNode variableName;
    ASTNode expr;
    public SetNode(AtomNode atomNode, ASTNode parse) {
        this.variableName = atomNode;
        this.expr  = parse;
    }

    @Override
    public Object eval(Environment env) {
        // (set! my-heart "like this marvelous ")
        env.define(variableName.value,expr);
        return "set!";
    }
}
