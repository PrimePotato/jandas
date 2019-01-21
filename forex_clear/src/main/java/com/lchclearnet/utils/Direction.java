package com.lchclearnet.utils;

public enum Direction {
    Buy(1), Sell(-1);

    int sign;

    Direction(int sign) {
        this.sign = sign;
    }

    public int sign() {
        return sign;
    }

    public Direction other() {
        return Buy == this ? Sell : Buy;
    }
}
