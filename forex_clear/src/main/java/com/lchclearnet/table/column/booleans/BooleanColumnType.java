package com.lchclearnet.table.column.booleans;

import com.lchclearnet.table.Column;
import com.lchclearnet.table.column.AbstractColumnType;
import com.lchclearnet.table.column.parsers.BooleanParser;

public class BooleanColumnType extends AbstractColumnType {

    public static final BooleanParser DEFAULT_PARSER = new BooleanParser();

    private static byte BYTE_SIZE = 1;

    public static final BooleanColumnType INSTANCE = new BooleanColumnType(BYTE_SIZE,"BOOLEAN","Boolean");

    private BooleanColumnType(int byteSize, String name, String printerFriendlyName) {
        super(byteSize, name, printerFriendlyName);
    }

    public static byte missingValueIndicator() {
        return Byte.MIN_VALUE;
    }

    @Override
    public BooleanColumn create(String name) {
        return BooleanColumn.create(name);
    }

    public Column<?> create(String name, int initialCapacity){
        return BooleanColumn.create(name, initialCapacity);
    }
}
