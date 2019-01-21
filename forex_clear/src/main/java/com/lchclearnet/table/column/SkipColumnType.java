package com.lchclearnet.table.column;

import com.lchclearnet.table.Column;
import com.lchclearnet.table.column.numbers.ShortColumn;

public class SkipColumnType extends AbstractColumnType {

    public static final SkipColumnType INSTANCE =
            new SkipColumnType(0, "SKIP", "Skipped");

    private SkipColumnType(int byteSize, String name, String printerFriendlyName) {
        super(byteSize, name, printerFriendlyName);
    }

    public static Object missingValueIndicator() {
        throw new UnsupportedOperationException("Column type " + SKIP + " doesn't support missing values");
    }

    @Override
    public Column<Void> create(String name) {
        throw new UnsupportedOperationException("Column type " + name() + " doesn't support column creation");
    }

    @Override
    public ShortColumn create(String name, int initialSize)  {

        throw new UnsupportedOperationException("Column type " + name() + " doesn't support column creation");
    }
}
