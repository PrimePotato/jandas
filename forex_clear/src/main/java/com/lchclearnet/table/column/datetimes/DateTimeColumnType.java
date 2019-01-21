package com.lchclearnet.table.column.datetimes;

import com.lchclearnet.table.column.AbstractColumnType;
import com.lchclearnet.table.column.dates.DateColumn;
import com.lchclearnet.table.column.parsers.DateTimeParser;

public class DateTimeColumnType extends AbstractColumnType {

    public static final DateTimeParser DEFAULT_PARSER = new DateTimeParser();
    public static int BYTE_SIZE = 8;
    public static final DateTimeColumnType INSTANCE = new DateTimeColumnType(BYTE_SIZE, "LOCAL_DATE_TIME", "DateTime");

    private DateTimeColumnType(int byteSize, String name, String printerFriendlyName) {
        super(byteSize, name, printerFriendlyName);
    }

    public static long missingValueIndicator() {
        return Long.MIN_VALUE;
    }

    @Override
    public DateTimeColumn create(String name) {
        return DateTimeColumn.create(name);
    }

    @Override
    public DateTimeColumn create(String name, int initialSize) { return DateTimeColumn.create(name, initialSize); }
}
