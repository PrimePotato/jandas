package com.lchclearnet.table.column.numbers;

import com.lchclearnet.table.column.AbstractColumnType;
import com.lchclearnet.table.column.parsers.LongParser;

public class LongColumnType extends AbstractColumnType {

    public static final LongParser DEFAULT_PARSER = new LongParser();

    private static final int BYTE_SIZE = 8;

    public static final LongColumnType INSTANCE = new LongColumnType(BYTE_SIZE, "LONG", "Long");

    private LongColumnType(int byteSize, String name, String printerFriendlyName) {
        super(byteSize, name, printerFriendlyName);
    }

    public static boolean isMissingValue(long value) {
        return value == missingValueIndicator();
    }

    public static long missingValueIndicator() {
        return Long.MIN_VALUE;
    }

    @Override
    public LongColumn create(String name) {
        return LongColumn.create(name);
    }

    @Override
    public LongColumn create(String name, int initialSize) {
        return LongColumn.create(name, initialSize);
    }
}
