package com.lchclearnet.utils;

public class FinalValue<T> {

    private T value;

    public FinalValue() {
        this(null);
    }

    public FinalValue(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
    }

    public boolean hasValue() {
        return this.value != null;
    }
}
