package io.github.primepotato.jandas.grouping;

import io.github.primepotato.jandas.column.Column;
import io.github.primepotato.jandas.dataframe.DataFrame;
import io.github.primepotato.jandas.dataframe.RecordSet;
import io.github.primepotato.jandas.utils.DataFrameUtils;
import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.ints.IntArrayList;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DataFrameGroupByNew<T> extends HashMap<T, DataFrame> {

    private final DataFrame dataFrame;
    private Function<RecordSet, T> groupFunction;
    private T[] index;
    private Map<T, IntArrayList> groupIndexes;
    private Class<T> cls;

    public DataFrameGroupByNew(Function<RecordSet, T> groupFunction, DataFrame df, Class<T> cls) {
        super();
        this.dataFrame = df;
        this.cls = cls;
        this.groupFunction = groupFunction;

        this.index = createIntMap();
        this.groupIndexes = assignGroups();
        this.groupIndexes.keySet().forEach(k -> this.put(k, getGroup(k)));
    }

    private T[] createIntMap() {
        final T[] index = (T[]) Array.newInstance(cls, dataFrame.rowCount());
        for (Iterator<RecordSet> it = dataFrame.recordSet(); it.hasNext(); ) {
            RecordSet rec = it.next();
            index[rec.getRowNumber()] = groupFunction.apply(rec);
        }
        return index;
    }

    private Map<T, IntArrayList> assignGroups() {
        Map<T, IntArrayList> grps = new HashMap<>();
        for (int i = 0; i < index.length; ++i) {
            IntArrayList l = grps.computeIfAbsent(index[i], k -> new IntArrayList());
            l.add(i);
        }
        return grps;
    }


    public DataFrame getGroup(T groupKey) {
        IntArrayList g = this.groupIndexes.get(groupKey);
        List<Column> columns = new ArrayList<>();
        for (Column c : dataFrame) {
            columns.add(c.subColumn("", Arrays.copyOf(g.elements(), g.size())));
        }
        return new DataFrame("", columns);
    }


}
