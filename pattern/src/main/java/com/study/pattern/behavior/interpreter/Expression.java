package com.study.pattern.behavior.interpreter;

public abstract class Expression {
    public abstract boolean interpret(Context context);

    public abstract boolean equals(Object object);

    public abstract int hashCode();

    public abstract String toString();
}