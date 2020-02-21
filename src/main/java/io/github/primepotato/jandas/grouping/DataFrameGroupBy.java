package io.github.primepotato.jandas.grouping;

import io.github.primepotato.jandas.column.Column;
import io.github.primepotato.jandas.column.impl.DoubleColumn;
//import io.github.primepotato.jandas.column.impl.StringColumn;
import io.github.primepotato.jandas.column.impl.ObjectColumn;
import io.github.primepotato.jandas.dataframe.DataFrame;
import io.github.primepotato.jandas.header.Heading;
import io.github.primepotato.jandas.index.meta.MetaIndex;
import io.github.primepotato.jandas.utils.DoubleAggregateFunc;
import it.unimi.dsi.fastutil.Function;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataFrameGroupBy {

    List<Column> gCols;
    List<DoubleColumn> aCols;
    MetaIndex metaIndex;

//    public DataFrameGroupBy(IndexFunction indexFunction) {
//
//    }

    public DataFrameGroupBy(List<Column> groupColumns, List<DoubleColumn> aggColumns) {
        this.gCols = groupColumns;
        this.aCols = aggColumns;
        metaIndex = DataFrame.buildMetaIndex(groupColumns);
    }

    public DataFrame aggregate(DoubleAggregateFunc daf) {
        return aggregate(x -> daf.apply((double[]) x));
    }

    public DataFrame aggregate(Function<double[], Double> daf) {
        Map<Heading, Object2DoubleOpenHashMap> results = new HashMap<>();
        for (DoubleColumn dc : aCols) {
            results.put(dc.getHeading(), metaIndex.aggregateDouble(dc.getData(), daf::apply));
        }
        return resolveToFrame(results);
    }

    private DataFrame resolveToFrame(Map<Heading, Object2DoubleOpenHashMap> aggResult) {

        List<Column> cols = new ArrayList();
        String[] labels = new String[0];
        int count = 0;
        for (Map.Entry<Heading, Object2DoubleOpenHashMap> e : aggResult.entrySet()) {
            double[] dVals = e.getValue().values().toArray(new double[0]);
            if (count == 0) {
                labels = (String[]) e.getValue().keySet().stream().map(x -> x.toString()).toArray(String[]::new);
            }
            cols.add(new DoubleColumn(e.getKey(), false, dVals));
            count++;
        }
        cols.add(0, new ObjectColumn<>(new Heading("groups"), false, labels, String.class));
        return new DataFrame("grouped", cols);
    }


}
