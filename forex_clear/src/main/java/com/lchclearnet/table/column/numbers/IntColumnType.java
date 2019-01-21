package com.lchclearnet.table.column.numbers;

import com.lchclearnet.table.column.AbstractColumnType;
import com.lchclearnet.table.column.parsers.IntParser;

public class IntColumnType extends AbstractColumnType {

    public static final IntParser DEFAULT_PARSER = new IntParser();

    private static final int BYTE_SIZE = 4;

    public static final IntColumnType INSTANCE = new IntColumnType(BYTE_SIZE, "INTEGER", "Integer");

    private IntColumnType(int byteSize, String name, String printerFriendlyName) {
        super(byteSize, name, printerFriendlyName);
    }

    public static boolean isMissingValue(int value) {
        return value == missingValueIndicator();
    }

    public static int missingValueIndicator() {
        return Integer.MIN_VALUE;
    }

    @Override
    public IntColumn create(String name) {
        return IntColumn.create(name);
    }

    @Override
    public IntColumn create(String name, int initialSize)  {
        return IntColumn.create(name, initialSize);
    }
}
