package io.github.primepotato.jandas.grouping;

import io.github.primepotato.jandas.column.Column;
import io.github.primepotato.jandas.dataframe.DataFrame;
import io.github.primepotato.jandas.dataframe.Record;
import io.github.primepotato.jandas.utils.DataFrameUtils;
import io.github.primepotato.jandas.utils.DoubleAggregateFunc;
import it.unimi.dsi.fastutil.ints.IntArrayList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataFrameGroupByNew {

    boolean simplified;
    DataFrame dataFrame;
    IndexFunction indexFunction;
    int[] index;
    Map<Integer, IntArrayList> groups;

    public DataFrameGroupByNew(IndexFunction indexFunction, DataFrame df) {
        this.simplified = false;
        this.dataFrame = df;
        this.indexFunction = indexFunction;
        this.index = createIntMap();
        this.groups = assignGroups();
    }

    public int[] createIntMap(){
        int[] index = new int[dataFrame.rowCount()];
        for (Record rec : dataFrame){
            index[rec.getRowNumber()] = indexFunction.apply(rec);
        }
        return index;
    }

    public Map<Integer, IntArrayList> assignGroups(){
        Map<Integer, IntArrayList> grps = new HashMap<>();
        for (int i=0; i<index.length; ++i){
            IntArrayList l = grps.computeIfAbsent(i, k->new IntArrayList());
            l.add(i);
        }
        return grps;
    }

    public DataFrame getGroup(int grp) {
        IntArrayList g = this.groups.get(grp);
        DataFrame df = DataFrameUtils.createEmptyDataFrameFromAnother(this.dataFrame);
        List<Record> recs =dataFrame.getRecords(g);
        recs.forEach(df::addRecord);
        return df;
    }

//    public DataFrame aggregate(DoubleAggregateFunc daf) {
//        return aggregate(x -> daf.apply((double[]) x));
//    }


}