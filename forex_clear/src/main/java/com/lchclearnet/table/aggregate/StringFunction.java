package com.lchclearnet.table.aggregate;

import com.lchclearnet.table.column.ColumnType;
import com.lchclearnet.table.column.strings.StringColumn;

/**
 * A partial implementation of aggregate functions to summarize over a date column
 */
public abstract class StringFunction extends AggregateFunction<StringColumn, String> {

    public StringFunction(String name) {
        super(name);
    }

    abstract public String summarize(StringColumn column);

    @Override
    public boolean isCompatibleColumn(ColumnType type) {
        return type.equals(ColumnType.STRING);
    }

    @Override
    public ColumnType returnType() {
        return ColumnType.STRING;
    }
}
