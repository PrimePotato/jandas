package com.lchclearnet.table.aggregate;

import com.lchclearnet.table.column.ColumnType;
import com.lchclearnet.table.column.dates.DateColumn;

import java.time.LocalDate;

/**
 * A partial implementation of aggregate functions to summarize over a date column
 */
public abstract class DateAggregateFunction extends AggregateFunction<DateColumn, LocalDate> {

    public DateAggregateFunction(String name) {
        super(name);
    }

    abstract public LocalDate summarize(DateColumn column);

    @Override
    public boolean isCompatibleColumn(ColumnType type) {
        return type.equals(ColumnType.LOCAL_DATE);
    }

    @Override
    public ColumnType returnType() {
        return ColumnType.LOCAL_DATE;
    }
}
