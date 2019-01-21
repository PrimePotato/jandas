package com.lchclearnet.table.column.numbers;

import com.lchclearnet.table.column.AbstractColumnType;
import com.lchclearnet.table.column.enums.EnumColumn;
import com.lchclearnet.table.column.parsers.FloatParser;

public class FloatColumnType extends AbstractColumnType {

    public static final int BYTE_SIZE = 4;

    public static final FloatParser DEFAULT_PARSER = new FloatParser();

    public static final FloatColumnType INSTANCE = new FloatColumnType(BYTE_SIZE, "FLOAT", "Float");

    private FloatColumnType(int byteSize, String name, String printerFriendlyName) {
        super(byteSize, name, printerFriendlyName);
    }

    public static boolean isMissingValue(float value) {
        return Float.isNaN(value);
    }

    public static float missingValueIndicator() {
        return Float.NaN;
    }

    @Override
    public FloatColumn create(String name) {
        return FloatColumn.create(name);
    }

    @Override
    public FloatColumn create(String name, int initialSize)  {
        return FloatColumn.create(name, initialSize);
    }
}
