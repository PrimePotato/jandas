package io.github.primepotato.jandas.grouping;

import io.github.primepotato.jandas.dataframe.Record;

import java.util.function.Function;

public class IndexFunction {

    private Function<Record, Integer> func;

    IndexFunction(Function<Record, Integer> func) {
        this.func = func;
    }

    public int apply(Record rec) {
        return this.func.apply(rec);
    }

}
