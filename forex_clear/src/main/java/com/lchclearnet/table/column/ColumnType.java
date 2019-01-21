package com.lchclearnet.table.column;

import com.google.common.base.Preconditions;
import com.lchclearnet.table.Column;
import com.lchclearnet.table.column.booleans.BooleanColumnType;
import com.lchclearnet.table.column.currency.CurrencyColumnType;
import com.lchclearnet.table.column.currencypair.CurrencyPairColumnType;
import com.lchclearnet.table.column.dates.DateColumnType;
import com.lchclearnet.table.column.datetimes.DateTimeColumnType;
import com.lchclearnet.table.column.numbers.*;
import com.lchclearnet.table.column.strings.StringColumnType;
import com.lchclearnet.table.column.strings.TextColumnType;
import com.lchclearnet.table.column.times.TimeColumnType;

import java.util.HashMap;
import java.util.Map;

public interface ColumnType {

    Map<String, ColumnType> values = new HashMap<>();

    // standard column types
    ShortColumnType SHORT = ShortColumnType.INSTANCE;
    IntColumnType INTEGER = IntColumnType.INSTANCE;
    LongColumnType LONG = LongColumnType.INSTANCE;
    FloatColumnType FLOAT = FloatColumnType.INSTANCE;
    BooleanColumnType BOOLEAN = BooleanColumnType.INSTANCE;
    StringColumnType STRING = StringColumnType.INSTANCE;
    DoubleColumnType DOUBLE = DoubleColumnType.INSTANCE;
    DateColumnType LOCAL_DATE = DateColumnType.INSTANCE;
    DateTimeColumnType LOCAL_DATE_TIME = DateTimeColumnType.INSTANCE;
    TimeColumnType LOCAL_TIME = TimeColumnType.INSTANCE;
    TextColumnType TEXT = TextColumnType.INSTANCE;
    CurrencyColumnType CURRENCY = CurrencyColumnType.INSTANCE;
    CurrencyPairColumnType CURRENCYPAIR = CurrencyPairColumnType.INSTANCE;

    SkipColumnType SKIP = SkipColumnType.INSTANCE;

    static void register(ColumnType type) {
        values.put(type.name(), type);
    }

    static ColumnType[] values() {
        return values.values().toArray(new ColumnType[0]);
    }

    static ColumnType valueOf(String name) {
        Preconditions.checkNotNull(name);

        ColumnType result = values.get(name);
        if (result == null) {
            throw new IllegalArgumentException(name + " is not a registered column type.");
        }
        return result;
    }

    Column<?> create(String name);

    Column<?> create(String name, int initialCapacity);

    String name();

    int byteSize();

    String getPrinterFriendlyName();

    default boolean compare(int rowNumber, Column<?> temp, Column<?> original) {
        return original.get(rowNumber).equals(temp.get(temp.size() - 1));
    }
}
