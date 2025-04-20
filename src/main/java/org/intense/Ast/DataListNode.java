package org.intense.Ast;

import org.intense.Environment;

public class DataListNode extends ASTNode{

    @Override
    public Result<Object> eval(Environment environment) {
        return Result.error(new Exception("This section Not Implemented"));
    }
}
