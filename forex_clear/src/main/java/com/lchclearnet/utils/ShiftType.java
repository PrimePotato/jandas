package com.lchclearnet.utils;

public enum ShiftType {

    PERCENT("P", "R", "PERCENT"), ABSOLUTE("A", "ABSOLUTE");

    private String[] values;

    ShiftType(String... values) {
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
