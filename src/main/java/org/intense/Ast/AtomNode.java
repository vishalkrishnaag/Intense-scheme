package org.intense.Ast;

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
    public Object eval(){
        if (type == TokenType.SYMBOL) {
            return value;
        }
        else if (type == TokenType.NUMBER){
            return Double.parseDouble(value);
        }
        return value;
    }

}