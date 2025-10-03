package org.intense;

import org.intense.Types.*;
import org.intense.ast.AtomNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Env {
    private final Env parent;
    public HashMap<String, Integer> SymbolTable = new HashMap<>();
    private final ArrayList<Value> values = new ArrayList<>();

    public ObjectManager getModuleManager() {
        return inheritanceManager;
    }

    public void setModuleManager(ObjectManager inheritanceManager) {
        this.inheritanceManager = inheritanceManager;
    }

    private ObjectManager inheritanceManager;
    private static int globalCounter = 0;
    private final Map<String, Value> builtIns = new HashMap<>();


    public Env() {
        this.parent = null;
    }

    public Env(Env parent) {
        this.inheritanceManager = new ObjectManager();
        this.parent = parent;
    }

    // Define a new variable in *this* scope
    public void define(String name, Value val) {
//        if (SymbolTable.containsKey(name)) {
//            throw new IllegalArgumentException("Symbol already exists in this scope: " + name);
//        }
        int id = globalCounter++;
        SymbolTable.put(name, id);

        // grow array if needed
        if (id >= values.size()) {
            values.add(val);
        } else {
            values.set(id, val);
        }
    }



    int getGlobalCounter(){
        return globalCounter;
    }

    // set assignment variable in *this* scope
    public void set(String name, Value val) {
        if (!SymbolTable.containsKey(name)) {
            throw new IllegalArgumentException("Symbol does not in this scope: " + name);
        }
        int id = globalCounter++;
        SymbolTable.put(name, id);

        // grow array if needed
        if (id >= values.size()) {
            values.add(val);
        } else {
            values.set(id, val);
        }
    }

    // Define a new variable in *this* scope
    public void defineSymbol(String name, Value val) {
        if (SymbolTable.containsKey(name)) {
            throw new IllegalArgumentException("Symbol already exists in this scope: " + name);
        }
        int id = globalCounter++;
        SymbolTable.put(name, id);

        // grow array if needed
        if (id >= values.size()) {
            values.add(val);
        } else {
            values.set(id, val);
        }
    }

    // Define a new variable in *this* scope
    public int getSymbolId(String name) {
        return SymbolTable.get(name);
    }
    public List<Value> getAllDataPresent() {
        return values;
    }
    public  Map<String,Integer> getSymbol(){
        return this.SymbolTable;
    }

    public void addSymbol(String name, int id) {
        if (SymbolTable.containsKey(name)) {
            throw new IllegalArgumentException("Symbol already exists in this scope: " + name);
        }
        SymbolTable.put(name, id);
    }


    // Edit an existing symbol in this scope
    public void edit(String name, Value val) {
        Integer id = SymbolTable.get(name);
        if (id != null) {
            values.set(id, val);
        } else if (parent != null) {
            // optionally edit in parent scope
            parent.edit(name, val);
        } else {
            throw new IllegalArgumentException("Symbol not found: " + name);
        }
    }


    // Lookup variable (searches up the parent chain)
    public Value lookup(String name) {
        Integer id = SymbolTable.get(name);
        if (id != null) return values.get(id);
        if (parent != null) return parent.lookup(name);
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

    // Direct lookup by ID (fastest path)
    public Value lookupById(int id) {
        if (id >= 0 && id < values.size()) return values.get(id);
        return null;
    }

    // Lookup variable (searches up the parent chain)
    public Value lookupBuiltIn(String name) {
        if (builtIns.containsKey(name)) {
            return builtIns.get(name);
        } else if (parent != null) {
            return parent.lookupBuiltIn(name);
        } else {
            // can be a native func ,eg (a.addSymbol? 4)
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
                AtomNode atom = new AtomNode(new VarVal(parts.getFirst(),false));
                Value val = atom.eval(this);

                return new PairFn(val, (BuiltIn) builtIns.get(parts.getLast()));
            }
            return new NullVal();
        }
    }
    public void defineBuiltIn(String key,Value value){
        this.builtIns.put(key,value);
    }

    public Map<String,Value> getBuiltIns(){
        return new HashMap<>(this.builtIns);
    }

    // Remove a symbol from this scope
    public void remove(String name) {
        Integer id = SymbolTable.remove(name);
        if (id != null) values.set(id, null); // mark as deleted
    }

    public int define(String name) {
        if (SymbolTable.containsKey(name)) {
            throw new IllegalArgumentException("Symbol already exists in this scope: " + name);
        }
        int id = globalCounter++;
        SymbolTable.put(name, id);
        if (id >= values.size()) {
            values.add(null);
        } else {
            values.set(id, null);
        }
        return id;
    }
}
