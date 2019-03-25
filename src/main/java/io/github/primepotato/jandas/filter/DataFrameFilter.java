package io.github.primepotato.jandas.filter;

import io.github.primepotato.jandas.dataframe.DataFrame;
import io.github.primepotato.jandas.dataframe.Record;
import io.github.primepotato.jandas.utils.DataFrameUtils;


import java.util.function.Predicate;

public class DataFrameFilter {

    private Predicate<Record> predicate;

    public DataFrameFilter(Predicate<Record> predicate){
        this.predicate = predicate;
    }

    Predicate<Record> getPredicate(){
        return predicate;
    }

    public DataFrame apply(DataFrame df){

        DataFrame frame = DataFrameUtils.createEmptyDataFrameFromAnother(df);
        for (Record rec: df){
            if (predicate.test(rec)) {
                frame.addRecord(rec);
            }
        }
        return frame;
    }

}
