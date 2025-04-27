package org.intense.Ast;

import org.intense.Environment;

public class PackageNode extends ASTNode{
    private String __package__;
    public PackageNode(String astNode) {
        this.__package__ = astNode;
    }

    @Override
    public Object eval(Environment env) {
        return null;
    }
}
