package io.github.primepotato.jandas.datatypes;

import io.github.primepotato.jandas.column.impl.DoubleColumn;
import io.github.primepotato.jandas.column.impl.IntegerColumn;
import io.github.primepotato.jandas.column.impl.ObjectColumn;

import java.util.Map;

public class dataTypeMapping {

    static Map<Class, Class> columnMap;

    static {

        columnMap.put(double.class, DoubleColumn.class);
        columnMap.put(Double.class, DoubleColumn.class);

        columnMap.put(int.class, IntegerColumn.class);
        columnMap.put(Integer.class, IntegerColumn.class);

        columnMap.put(String.class, ObjectColumn.class);

    }

}
