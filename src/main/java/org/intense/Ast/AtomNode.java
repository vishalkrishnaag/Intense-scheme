package org.intense.Ast;

import org.intense.Environment;
import org.intense.TokenType;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    public Object apply(ListNode args, List<ASTNode> body) {
        try {
            Object result = null;
            //1 st is lambda
            for (ASTNode astNode : body) {
                result = astNode.eval(null);
            }
            return Optional.ofNullable(result);
        } catch (Exception e) {
            throw new RuntimeException("closure application failed");
        }
    }

    @Override
    public Object eval(Environment env) {
        if (type == TokenType.SYMBOL) {;
            if(env !=null)
            {
                List<ASTNode> data= env.lookup(value);
                //todo: need rework
            }
            return value;
        }
        else if (type == TokenType.NUMBER){
            return Double.parseDouble(value);
        }
        return value;
    }
}