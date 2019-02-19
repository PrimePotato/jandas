package io.github.primepotato.jandas.dataframe;

import io.github.primepotato.jandas.column.AbstractColumn;
import io.github.primepotato.jandas.column.Column;
import io.github.primepotato.jandas.column.DoubleColumn;
import io.github.primepotato.jandas.column.StringColumn;
import io.github.primepotato.jandas.index.meta.MetaIndex;
import io.github.primepotato.jandas.utils.DoubleAggregateFunc;
import it.unimi.dsi.fastutil.ints.Int2DoubleOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;

import javax.sound.midi.SysexMessage;
import java.util.*;

public class DataFrameGroupBy {


    List<Column> gCols;
    List<DoubleColumn> aCols;
    MetaIndex metaIndex;

    public DataFrameGroupBy(List<Column> groupColumns, List<DoubleColumn> aggColumns) {
        this.gCols = groupColumns;
        this.aCols = aggColumns;
        metaIndex = DataFrame.buildMetaIndex(groupColumns);
    }

    public DataFrame aggregate(DoubleAggregateFunc daf) {
        Map<String, Object2DoubleOpenHashMap> results = new HashMap<>();
        for (DoubleColumn dc : aCols) {
            results.put(dc.name, metaIndex.aggregateDouble(dc.rawData(), daf));
        }

        return resolveToFrame(results);
    }

    public DataFrame resolveToFrame(Map<String, Object2DoubleOpenHashMap> aggResult) {

        List<Column> cols = new ArrayList();
        String[] labels= new String[0];
        int count = 0;
        for (Map.Entry<String, Object2DoubleOpenHashMap> e : aggResult.entrySet()) {
            double[] dVals = e.getValue().values().toArray(new double[0]);
            if (count == 0) {
                labels = (String[])e.getValue().keySet().stream().map(x->x.toString()).toArray(String[]::new);;
            }
            cols.add(new DoubleColumn(e.getKey(), false, dVals));
            count++;
        }
        cols.add(0, new StringColumn("groups", false, labels));
        return new DataFrame("grouped", cols);
    }


}
