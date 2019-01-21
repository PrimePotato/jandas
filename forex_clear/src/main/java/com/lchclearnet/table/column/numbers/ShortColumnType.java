package com.lchclearnet.table.column.numbers;

import com.lchclearnet.table.column.AbstractColumnType;
import com.lchclearnet.table.column.parsers.ShortParser;

public class ShortColumnType extends AbstractColumnType {

    public static final ShortParser DEFAULT_PARSER = new ShortParser();

    private static final int BYTE_SIZE = 2;

    public static final ShortColumnType INSTANCE = new ShortColumnType(BYTE_SIZE, "SHORT", "Short");

    private ShortColumnType(int byteSize, String name, String printerFriendlyName) {
        super(byteSize, name, printerFriendlyName);
    }

    public static boolean isMissingValue(int value) {
        return value == missingValueIndicator();
    }

    public static short missingValueIndicator() {
        return Short.MIN_VALUE;
    }

    @Override
    public ShortColumn create(String name) {
        return ShortColumn.create(name);
    }

    @Override
    public ShortColumn create(String name, int initialSize)  {
        return ShortColumn.create(name, initialSize);
    }
}
