package com.lchclearnet.table.column.strings;

import com.lchclearnet.table.column.AbstractColumnType;
import com.lchclearnet.table.column.ColumnType;
import com.lchclearnet.table.column.parsers.StringParser;

public class TextColumnType extends AbstractColumnType {

    public static final int BYTE_SIZE = 4;
    public static final StringParser DEFAULT_PARSER = new StringParser();

    public static final TextColumnType INSTANCE = new TextColumnType(BYTE_SIZE,"TEXT","Text");

    private TextColumnType(int byteSize, String name, String printerFriendlyName) {
        super(byteSize, name, printerFriendlyName);
    }

    public static String missingValueIndicator() {
        return "";
    }

    @Override
    public TextColumn create(String name) {
        return TextColumn.create(name);
    }

    @Override
    public TextColumn create(String name, int initialSize) {
        return TextColumn.create(name, initialSize);
    }

}
