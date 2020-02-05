package io.github.primepotato.jandas.utils;

import io.github.primepotato.jandas.column.Column;
import io.github.primepotato.jandas.dataframe.DataFrame;

import java.util.ArrayList;
import java.util.List;

public class DataFrameUtils {

    public static DataFrame createEmptyDataFrameFromAnother(DataFrame df) {
        List<Column> newFrame = new ArrayList<>();
        for (Column col : df) {
            newFrame.add(col.createEmpty());
        }
        return new DataFrame("copy" + df.name(), newFrame);
    }

}
