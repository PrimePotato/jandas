package com.lchclearnet.table.column.dates;

import com.lchclearnet.table.column.AbstractColumnType;
import com.lchclearnet.table.column.currencypair.CurrencyPairColumn;
import com.lchclearnet.table.column.parsers.DateParser;

public class DateColumnType extends AbstractColumnType {

    public static final int BYTE_SIZE = 4;
    public static final DateParser DEFAULT_PARSER = new DateParser();

    public static final DateColumnType INSTANCE = new DateColumnType(BYTE_SIZE, "LOCAL_DATE", "Date");

    private DateColumnType(int byteSize, String name, String printerFriendlyName) {
        super(byteSize, name, printerFriendlyName);
    }

    public static int missingValueIndicator() {
        return Integer.MIN_VALUE;
    }

    @Override
    public DateColumn create(String name) {
        return DateColumn.create(name);
    }

    @Override
    public DateColumn create(String name, int initialSize) { return DateColumn.create(name, initialSize); }
}
