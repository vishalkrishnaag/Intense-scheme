package org.intense.Ast;

import org.intense.Environment;

public class DisplayNode extends ASTNode {
    public ASTNode getPrintable() {
        return printable;
    }

    public void setPrintable(ASTNode printable) {
        this.printable = printable;
    }

    ASTNode printable = null;

    @Override
    public Object eval(Environment env) {
        System.out.println(printable.eval(null));
        return "()";
    }
}
