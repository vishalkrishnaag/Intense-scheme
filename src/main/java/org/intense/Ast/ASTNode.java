package org.intense.Ast;


import org.intense.Environment;
import org.intense.TokenType;

import java.util.List;
import java.util.Map;

public abstract class ASTNode {
    public abstract Object eval(Environment env);
    private TokenType getDataType(TokenType type1, TokenType type2) {
        if (type1 == type2) {
            return type1;
        } else if (type1 == TokenType.DOUBLE && type2 == TokenType.BOOLEAN) {
            return TokenType.DOUBLE;
        } else if (type1 == TokenType.BOOLEAN && type2 == TokenType.DOUBLE) {
            return TokenType.DOUBLE;
        } else if (type1 == TokenType.INT && type2 == TokenType.DOUBLE) {
            return TokenType.DOUBLE;
        } else if (type1 == TokenType.DOUBLE && type2 == TokenType.INT) {
            return TokenType.DOUBLE;
        } else if (type1 == TokenType.STRING && type2 == TokenType.INT) {
            return TokenType.STRING;
        } else if (type1 == TokenType.INT && type2 == TokenType.STRING) {
            return TokenType.STRING;
        } else {
            throw new RuntimeException("TokenType is invalid");
        }
    }
}
