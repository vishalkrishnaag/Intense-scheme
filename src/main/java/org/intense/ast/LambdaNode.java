package org.intense.ast;

import org.intense.Env;
import org.intense.Types.FnVal;
import org.intense.Types.LambdaFn;
import org.intense.Types.Value;

import java.util.List;

public class LambdaNode extends ASTNode {
    private final List<String> parameters;
    private final List<ASTNode> body;

    public LambdaNode(List<String> parameters, List<ASTNode> body) {
        this.parameters = parameters;
        this.body = body;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public List<ASTNode> getBody() {
        return body;
    }

    @Override
    public Value eval(Env env) {
        // Instead of executing immediately, return a closure
        return new LambdaFn(parameters, body, env);
    }

    @Override
    public String toString() {
        return "(lambda " + parameters + " " + body + ")";
    }
}
