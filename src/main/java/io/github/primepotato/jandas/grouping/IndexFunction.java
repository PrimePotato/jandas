package io.github.primepotato.jandas.grouping;

import io.github.primepotato.jandas.dataframe.RecordSet;

import java.util.function.Function;

public class IndexFunction {

    private Function<RecordSet, Integer> func;

    IndexFunction(Function<RecordSet, Integer> func) {
        this.func = func;
    }

    public int apply(RecordSet rec) {
        return this.func.apply(rec);
    }

}
