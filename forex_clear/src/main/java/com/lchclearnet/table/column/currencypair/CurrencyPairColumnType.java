package com.lchclearnet.table.column.currencypair;

import com.lchclearnet.table.column.AbstractColumnType;
import com.lchclearnet.table.column.currency.CurrencyColumn;
import com.lchclearnet.table.column.parsers.CurrencyPairParser;

public class CurrencyPairColumnType extends AbstractColumnType {

    public static final CurrencyPairParser DEFAULT_PARSER = new CurrencyPairParser();
    private static byte BYTE_SIZE = 4;
    public static final CurrencyPairColumnType INSTANCE = new CurrencyPairColumnType(BYTE_SIZE, "CURRENCY", "CurrencyPair");

    private CurrencyPairColumnType(int byteSize, String name, String printerFriendlyName) {
        super(byteSize, name, printerFriendlyName);
    }

    public static int missingValueIndicator() {
        return Integer.MIN_VALUE;
    }

    @Override
    public CurrencyPairColumn create(String name) {
        return CurrencyPairColumn.create(name);
    }

    @Override
    public CurrencyPairColumn create(String name, int initialSize) { return CurrencyPairColumn.create(name, initialSize); }
}
