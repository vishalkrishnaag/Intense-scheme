package org.intense.Types;
import org.intense.Env;
import org.intense.ast.ASTNode;
import org.intense.ast.AtomNode;
import org.intense.ast.CallNode;

import java.util.List;

public class ReferenceValue extends Value{

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    private String parent;

    public ReferenceValue(String parent) {
        this.parent = parent;
    }
}
