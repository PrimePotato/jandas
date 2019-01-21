package com.lchclearnet.table.column.times;

import com.lchclearnet.table.column.AbstractColumnType;
import com.lchclearnet.table.column.parsers.TimeParser;

public class TimeColumnType extends AbstractColumnType {

    public static final int BYTE_SIZE = 4;

    public static final TimeParser DEFAULT_PARSER = new TimeParser();
    public static final TimeColumnType INSTANCE = new TimeColumnType(BYTE_SIZE, "LOCAL_TIME", "Time");

    private TimeColumnType(int byteSize, String name, String printerFriendlyName) {
        super(byteSize, name, printerFriendlyName);
    }

    public static int missingValueIndicator() {
        return Integer.MIN_VALUE;
    }

    @Override
    public TimeColumn create(String name) {
        return TimeColumn.create(name);
    }

    @Override
    public TimeColumn create(String name, int initialSize) {
        return TimeColumn.create(name, initialSize);
    }
}
