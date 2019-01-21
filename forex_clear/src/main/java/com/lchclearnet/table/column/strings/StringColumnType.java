package com.lchclearnet.table.column.strings;

import com.lchclearnet.table.column.AbstractColumnType;
import com.lchclearnet.table.column.ColumnType;
import com.lchclearnet.table.column.parsers.StringParser;
import com.lchclearnet.table.column.times.TimeColumn;

public class StringColumnType extends AbstractColumnType {

    public static final int BYTE_SIZE = 4;
    public static final StringParser DEFAULT_PARSER = new StringParser();

    public static final StringColumnType INSTANCE = new StringColumnType(BYTE_SIZE, "STRING","String");

    private StringColumnType(int byteSize, String name, String printerFriendlyName) {
        super(byteSize, name, printerFriendlyName);
    }

    public static String missingValueIndicator() {
        return "";
    }

    @Override
    public StringColumn create(String name) {
        return StringColumn.create(name);
    }

    @Override
    public StringColumn create(String name, int initialSize) {
        return StringColumn.create(name, initialSize);
    }

}
