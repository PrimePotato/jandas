package com.lchclearnet.table.aggregate;

import com.lchclearnet.table.Column;
import com.lchclearnet.table.column.ColumnType;

abstract class CountFunction extends AggregateFunction<Column<?>, Integer> {

    public CountFunction(String functionName) {
        super(functionName);
    }

    @Override
    abstract public Integer summarize(Column<?> column);

    @Override
    public boolean isCompatibleColumn(ColumnType type) {
        return true;
    }

    @Override
    public ColumnType returnType() {
        return ColumnType.DOUBLE;
    }
}
