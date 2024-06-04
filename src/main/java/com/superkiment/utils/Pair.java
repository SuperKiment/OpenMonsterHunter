package com.superkiment.utils;

/**
 * A pair to return two values of different types in one return. getFirst()
 * returns the value stored in the first class, and getSecond() returns the
 * value stored in the second class.
 */
public class Pair<T1, T2> {
    private T1 t1;
    private T2 t2;

    public Pair(T1 t1, T2 t2) {
        this.t1 = t1;
        this.t2 = t2;
    }

    public T1 getFirst() {
        return this.t1;
    }

    public void setFirst(T1 t1) {
        this.t1 = t1;
    }

    public T2 getSecond() {
        return this.t2;
    }

    public void setSecond(T2 t2) {
        this.t2 = t2;
    }
}
