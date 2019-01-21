package com.lchclearnet.utils;

public class DoubleValue {

    private double value;

    public DoubleValue() {
        this(Double.NaN);
    }

    public DoubleValue(double value) {
        set(value);
    }

    public double get() {
        return value;
    }

    public void set(double value) {
        this.value = value;
    }

    public boolean hasValue() {
        return !Double.isNaN(value);
    }
}
