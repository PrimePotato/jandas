package com.lchclearnet.report.morpheus;

public abstract class AbstractRowGroup<R, C> implements RowGroup<R, C> {

    private final C[] columns;

    public AbstractRowGroup(C[] columns) {
        this.columns = columns;
    }

    @Override
    public C[] columns() {
        return columns;
    }
}
