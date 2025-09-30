package org.intense.ast;

import org.intense.Env;
import org.intense.Types.*;

import java.util.List;

public class AtomNode extends ASTNode {
    public Value getValue() {
        return value;
    }

    public void setValue(Value valueProvided) {
        this.value = valueProvided;
    }

    private Value value;  // already a runtime Value
    private boolean mutable = false; // if you want mutability flag

    public AtomNode(Value value) {
        this.value = value;
    }

    @Override
    public Value eval(Env env) {
        System.out.println("Evaluating AtomNode: " + value);
            // If it's an identifier (symbol), look it up
            if (value instanceof VarVal idVal) {
                Value resolved = env.lookup(idVal.value);
                if (resolved == null) {
                    throw new RuntimeException("Undefined identifier: " + idVal.value);
                }
                else if (resolved instanceof ReferenceValue reference) {
                    return env.lookup(reference.getParent());
                }
                return resolved;
            }
            // Otherwise, it's already a literal (NumVal, StrVal, etc.)
            return value;
    }

    @Override
    public String toString() {
        return "Atom(" + value + ")";
    }

    public boolean isMutable() {
        return mutable;
    }

    public void setMutable(boolean mutable) {
        this.mutable = mutable;
    }
}
