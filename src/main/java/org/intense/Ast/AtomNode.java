package org.intense.Ast;

import org.intense.Environment;
import org.intense.TokenType;

public class AtomNode extends ASTNode {
    TokenType type;

    public AtomNode(TokenType type, String value) {
        this.value = value;
        this.type = type;
    }

    public String value;


    public String toString() {
        return type.toString()+"(" + value + ")";

    }

    @Override
    public Result<Object> eval(Environment environment){
        if (type == TokenType.SYMBOL) {
            return Result.ok(environment.lookup(value));
        }
        else if (type == TokenType.NUMBER){
            return Result.ok(Double.parseDouble(value));
        }
        return Result.ok(value);
    }

}