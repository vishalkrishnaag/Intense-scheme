package org.intense;

import org.intense.Types.*;
import org.intense.ast.AtomNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Env {
    private final Env parent;
    private final Map<String, Value> symbols = new HashMap<>();
    private final Map<String, Value> builtIns = new HashMap<>();

    public Env() {
        this.parent = null;
    }

    public Env(Env parent) {
        this.parent = parent;
    }

    // Define a new variable in *this* scope
    public void define(String name, Value value) {
        symbols.put(name, value);
    }

    // Lookup variable (searches up the parent chain)
    public Value lookup(String name) {
        if (symbols.containsKey(name)) {
            return symbols.get(name);
        } else if (parent != null) {
            return parent.lookup(name);
        } else {
            int dotIndex = name.indexOf(".");
            String root = dotIndex == -1 ? name : name.substring(0, dotIndex);
            String remainder = dotIndex == -1 ? "" : name.substring(dotIndex);

            if(dotIndex!=-1)
            {
                Value val = this.lookup(root);
                if (val instanceof ReferenceValue ref) {
                    // replace alias with parent name
                    root = ref.getParent();
                    name = root + remainder;
                   return this.lookup(name);
                }
            }
            return lookupBuiltIn(name);
        }
    }

    // Lookup variable (searches up the parent chain)
    public Value lookupBuiltIn(String name) {
        if (builtIns.containsKey(name)) {
            return builtIns.get(name);
        } else if (parent != null) {
            return parent.lookupBuiltIn(name);
        } else {
            // can be a native func ,eg (a.add? 4)
            int dotIndex = name.indexOf('.');
            List<String> parts = new ArrayList<>();
            if (dotIndex != -1) {
                parts.add(name.substring(0, dotIndex));
                parts.add(name.substring(dotIndex + 1));
            } else {
                parts.add(name); // no dot found
            }
            if(builtIns.containsKey(parts.getLast()))
            {
                AtomNode atom = new AtomNode(new VarVal(parts.getFirst()));
                Value val = atom.eval(this);
                return new PairFn(val, (BuiltIn) builtIns.get(parts.getLast()));
            }
            return new NullVal();
        }
    }
    public void defineBuiltIn(String key,Value value){
        this.builtIns.put(key,value);
    }
    public Map<String,Value> getSymbol(){
       return new HashMap<>(this.symbols);
    }
    public Map<String,Value> getBuiltIns(){
        return new HashMap<>(this.builtIns);
    }

    // Set existing variable (mutates closest scope where it's defined)
    public void set(String name, Value value) {
        if (symbols.containsKey(name)) {
            symbols.put(name, value);
        } else if (parent != null) {
            parent.set(name, value);
        } else {
            throw new RuntimeException("Undefined symbol: " + name);
        }
    }
}
