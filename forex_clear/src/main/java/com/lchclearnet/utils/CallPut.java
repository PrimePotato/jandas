package com.lchclearnet.utils;

public enum CallPut {

    Call("Call"), Put("Put");

    private String[] values;

    CallPut(String... values) {
        this.values = values;
    }

    public String[] getValues() {
        return values;
    }

    @Override
    public String toString() {
        return values[0];
    }
}
