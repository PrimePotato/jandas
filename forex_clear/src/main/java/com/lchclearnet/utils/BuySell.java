package com.lchclearnet.utils;

public enum BuySell {

    Buy("Buy"), Sell("Sell");

    private String[] values;

    BuySell(String... values) {
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
