package io.github.primepotato.jandas.dataframe;

import io.github.primepotato.jandas.column.Column;

import java.util.List;
import java.util.function.Function;

public class DataFrameFilter {

    List<Integer> inputColumns;
    Function <List<Column>, Boolean> filterFunc;
    boolean[] dataframeMask;

    void apply(DataFrame df){
        for (int i=0; i<inputColumns.size();++i) {
            df.column(i);
        }
    }

}
