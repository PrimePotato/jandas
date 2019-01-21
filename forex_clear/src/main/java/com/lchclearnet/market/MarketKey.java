package com.lchclearnet.market;

public class MarketKey {

    public final DataType type;

    public final String[] value;

    public MarketKey(DataType type, String... value) {
        this.type = type;
        this.value = value;
    }

}
