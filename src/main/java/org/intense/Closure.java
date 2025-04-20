package org.intense;

import org.intense.Ast.ASTNode;
import org.intense.Ast.AtomNode;
import org.intense.Ast.ListNode;

import java.util.List;
import java.util.Optional;

public class Closure {
    private final ListNode params;
    private final ASTNode body;
    private final Environment env;

    public Closure(ListNode params, ASTNode body, Environment env) {
        this.params = params;
        this.body = body;
        this.env = env;
    }

    public Optional<Object> apply(List<Object> args) {
        if (args.size() != params.elements.size()) {
            return Optional.empty();  // Argument count mismatch
        }

        Environment localEnv = new Environment(env);
        for (int i = 0; i < params.elements.size(); i++) {
            AtomNode atom = (AtomNode) params.elements.get(i);
            localEnv.define(atom.value, args.get(i));
        }

        try {
            return Optional.ofNullable(body.eval(localEnv));
        } catch (Exception e) {
            e.printStackTrace();  // Log it, don't crash thread
            return Optional.empty();
        }
    }
    @Override
    public String toString() {
        return "(closure)";
    }
}
