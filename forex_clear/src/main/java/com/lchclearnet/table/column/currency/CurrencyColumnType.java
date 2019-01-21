package com.lchclearnet.table.column.currency;

import com.lchclearnet.table.column.AbstractColumnType;
import com.lchclearnet.table.column.numbers.LongColumn;
import com.lchclearnet.table.column.parsers.CurrencyParser;

public class CurrencyColumnType extends AbstractColumnType {

    public static final CurrencyParser DEFAULT_PARSER = new CurrencyParser();
    private static byte BYTE_SIZE = 4;
    public static final CurrencyColumnType INSTANCE = new CurrencyColumnType(BYTE_SIZE, "CURRENCY", "Currency");

    private CurrencyColumnType(int byteSize, String name, String printerFriendlyName) {
        super(byteSize, name, printerFriendlyName);
    }

    public static int missingValueIndicator() {
        return Integer.MIN_VALUE;
    }

    @Override
    public CurrencyColumn create(String name) {
        return CurrencyColumn.create(name);
    }

    @Override
    public CurrencyColumn create(String name, int initialSize) {
        return CurrencyColumn.create(name, initialSize);
    }
}
