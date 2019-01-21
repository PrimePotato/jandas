package com.lchclearnet.table.aggregate;

import com.lchclearnet.table.Column;
import com.lchclearnet.table.column.ColumnType;

/**
 * A partial implementation of aggregate functions to summarize over a numeric column
 */
public abstract class AggregateFunction<INCOL extends Column<?>, OUT> {

    private final String functionName;

    public AggregateFunction(String functionName) {
        this.functionName = functionName;
    }

    public String functionName() {
        return functionName;
    }

    public abstract OUT summarize(INCOL column);

    public String toString() {
        return functionName();
    }

    public abstract boolean isCompatibleColumn(ColumnType type);

    public abstract ColumnType returnType();
}
