package org.intense.ast;

import org.intense.Env;
import org.intense.Types.BoolVal;
import org.intense.Types.NullVal;
import org.intense.Types.Value;

public class IfConditionNode extends ASTNode{
    private ASTNode ifExp;
    private ASTNode ifBody;
    private ASTNode elseBody;

    public IfConditionNode(ASTNode ifExpr, ASTNode dtifBody, ASTNode dtelseBody) {
        ifExp = ifExpr;
        ifBody = dtifBody;
        elseBody = dtelseBody;
    }


    @Override
    public Value eval(Env env) {
        Value exp = ifExp.eval(env);
        if (exp instanceof BoolVal boolVal)
        {
            if(boolVal.value)
            {
                if (ifBody != null) {
                 return ifBody.eval(env);
                }
            }
            else {
                if (elseBody != null) {
                return elseBody.eval(env);
                }
            }
            return new NullVal();
        }
        else {
            throw new RuntimeException("if expression must return a boolean value as output");
        }
    }
}
