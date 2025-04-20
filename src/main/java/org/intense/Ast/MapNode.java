package org.intense.Ast;

import org.intense.Environment;

public class MapNode extends ASTNode{
    @Override
    public Result<Object> eval(Environment environment)  {
        return Result.error(new Exception("this Section is under development"));
    }
}
