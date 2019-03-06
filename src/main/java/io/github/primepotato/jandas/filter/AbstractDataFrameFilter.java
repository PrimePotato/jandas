package io.github.primepotato.jandas.filter;

import io.github.primepotato.jandas.dataframe.DataFrame;
import io.github.primepotato.jandas.dataframe.Record;
import io.github.primepotato.jandas.utils.DataFrameUtils;


import java.util.function.Predicate;

public abstract class AbstractDataFrameFilter {

    abstract Predicate<Record> getPredicate();

    DataFrame apply(DataFrame df){

        DataFrame frame = DataFrameUtils.createEmptyDataFrameFromAnother(df);
        Predicate<Record> ped = getPredicate();
        for (Record rec: df){
            if (ped.test(rec)) {
                frame.addRecord(rec);
            }
        }
        return frame;
    }

}
