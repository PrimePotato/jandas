package com.lchclearnet.report.morpheus;

import com.zavtech.morpheus.frame.DataFrameRow;
import com.lchclearnet.utils.Tuple;

public interface RowGroup<R, C> {
    C[] columns();

    Tuple index(DataFrameRow<R, C> row);
}
