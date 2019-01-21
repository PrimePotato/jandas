package com.lchclearnet.table.aggregate;

import com.lchclearnet.table.column.ColumnType;
import com.lchclearnet.table.column.booleans.BooleanColumn;

abstract class BooleanCountFunction extends AggregateFunction<BooleanColumn, Integer> {

    public BooleanCountFunction(String functionName) {
        super(functionName);
    }

    @Override
    abstract public Integer summarize(BooleanColumn column);

    @Override
    public boolean isCompatibleColumn(ColumnType type) {
        return type.equals(ColumnType.BOOLEAN);
    }

    @Override
    public ColumnType returnType() {
        return ColumnType.DOUBLE;
    }
}
