package com.lchclearnet.table.aggregate;

import com.lchclearnet.table.column.ColumnType;
import com.lchclearnet.table.column.times.TimeColumn;

import java.time.LocalTime;

/**
 * A partial implementation of aggregate functions to summarize over a time column
 */
public abstract class TimeAggregateFunction extends AggregateFunction<TimeColumn, LocalTime> {

    public TimeAggregateFunction(String name) {
        super(name);
    }

    abstract public LocalTime summarize(TimeColumn column);

    @Override
    public boolean isCompatibleColumn(ColumnType type) {
        return type.equals(ColumnType.LOCAL_TIME);
    }

    @Override
    public ColumnType returnType() {
        return ColumnType.LOCAL_TIME;
    }
}
