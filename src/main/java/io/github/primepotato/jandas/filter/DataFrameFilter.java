package io.github.primepotato.jandas.filter;

import io.github.primepotato.jandas.dataframe.DataFrame;
import io.github.primepotato.jandas.dataframe.RecordSet;
import io.github.primepotato.jandas.utils.DataFrameUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;


import java.util.Iterator;
import java.util.function.Predicate;

@AllArgsConstructor
@Getter
public class DataFrameFilter {

    private Predicate<RecordSet> predicate;

    public DataFrame apply(DataFrame df){

        DataFrame frame = DataFrameUtils.createEmptyDataFrameFromAnother(df);
        for (Iterator<RecordSet> it = df.recordSet(); it.hasNext(); ) {
            RecordSet rec = it.next();
            if (predicate.test(rec)) {
                frame.addRecord(rec);
            }
        }
        return frame;
    }

}
