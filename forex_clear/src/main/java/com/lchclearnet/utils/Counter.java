package com.lchclearnet.utils;

public class Counter {

    private int count;
    private int cursor;

    public Counter(int intialVal) {
        count = intialVal;
        cursor = intialVal;
    }

    public int increment() {
        return cursor++;
    }

    public int decrement() {
        return cursor--;
    }

    public int count() {
        return count;
    }

    public int get() {
        return cursor;
    }
}
