package io.github.primepotato.jandas.dataframe;

import io.github.primepotato.jandas.column.Column;

import java.util.Map;

public class Row {

    public Map<Class, Map<String, ?>> data;
    public DataFrame df;
    public int rowIndex;

    public Row(int rowIndex, DataFrame df) {
        this.df = df;
        this.rowIndex = rowIndex;
    }

    public void getData() {
        for (int i = 0; i < df.columnCount(); ++i) {
            Column column = df.column(i);
//            data.computeIfAbsent(column.getClass(), x -> new HashMap<String, ?>());
        }
    }

//    public Map<String, ?> getDataMap(Class cls) {
//        return  new HashMap<String, cls>();
//    }

}
