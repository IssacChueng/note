package com.study.pattern.behavior.interpreter;

public class Variable extends Expression {

    private String name;

    public Variable(String name) {
        this.name = name;
    }

    @Override
    public boolean interpret(Context context) {
        boolean lookup = context.lookup(this);
        return lookup;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object object) {
        if (object != null && object instanceof Variable) {
            return name.equals(((Variable)object).name);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public String toString() {
        return null;
    }
    
    
}