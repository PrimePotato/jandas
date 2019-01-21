package com.lchclearnet.table.column.numbers;

import com.lchclearnet.table.column.AbstractColumnType;
import com.lchclearnet.table.column.datetimes.DateTimeColumn;
import com.lchclearnet.table.column.parsers.DoubleParser;

public class DoubleColumnType extends AbstractColumnType {

    public static final DoubleParser DEFAULT_PARSER = new DoubleParser();
    private static final int BYTE_SIZE = 8;
    public static final DoubleColumnType INSTANCE = new DoubleColumnType(BYTE_SIZE, "DOUBLE", "Double");

    private DoubleColumnType(int byteSize, String name, String printerFriendlyName) {
        super(byteSize, name, printerFriendlyName);
    }

    public static boolean isMissingValue(double value) {
        return Double.isNaN(value);
    }

    public static double missingValueIndicator() {
        return Double.NaN;
    }

    @Override
    public DoubleColumn create(String name) {
        return DoubleColumn.create(name);
    }

    @Override
    public DoubleColumn create(String name, int initialSize) { return DoubleColumn.create(name, initialSize); }
}
