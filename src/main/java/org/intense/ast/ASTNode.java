package org.intense.ast;


import org.intense.Env;
import org.intense.Types.Value;

public abstract class ASTNode {
    public abstract Value eval(Env env);
}
