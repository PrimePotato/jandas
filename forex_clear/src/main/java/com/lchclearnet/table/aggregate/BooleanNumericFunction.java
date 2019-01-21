package com.lchclearnet.table.aggregate;

import com.lchclearnet.table.column.ColumnType;
import com.lchclearnet.table.column.booleans.BooleanColumn;

abstract class BooleanNumericFunction extends AggregateFunction<BooleanColumn, Double> {

    public BooleanNumericFunction(String functionName) {
        super(functionName);
    }

    @Override
    abstract public Double summarize(BooleanColumn column);

    @Override
    public boolean isCompatibleColumn(ColumnType type) {
        return type.equals(ColumnType.BOOLEAN);
    }

    @Override
    public ColumnType returnType() {
        return null;
    }
}
