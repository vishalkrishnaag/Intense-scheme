package org.intense.Types;

public class InstanceValue extends Value{

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    private String parent;

    public InstanceValue(String parent) {
        this.parent = parent;
    }
}
