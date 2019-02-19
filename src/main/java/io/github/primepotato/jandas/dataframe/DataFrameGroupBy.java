package io.github.primepotato.jandas.dataframe;

import io.github.primepotato.jandas.column.Column;
import io.github.primepotato.jandas.column.DoubleColumn;
import io.github.primepotato.jandas.index.meta.MetaIndex;
import io.github.primepotato.jandas.utils.DoubleAggregateFunc;
import it.unimi.dsi.fastutil.ints.Int2DoubleOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DataFrameGroupBy implements Iterable<DataFrame> {


    List<Column> gCols;
    List<DoubleColumn> aCols;
    MetaIndex metaIndex;

    public DataFrameGroupBy(List<Column> gCols, List<DoubleColumn> aCols) {
        this.gCols = gCols;
        this.aCols = aCols;
        metaIndex = DataFrame.buildMetaIndex(gCols);
    }

    @Override
    public Iterator<DataFrame> iterator() {

        return null;
    }

    public Map<String, Object2DoubleOpenHashMap> aggregate(DoubleAggregateFunc daf) {
        Map<String, Object2DoubleOpenHashMap> results = new HashMap<>();
        for (DoubleColumn dc : aCols) {
            results.put(dc.name, metaIndex.aggregateDouble(dc.rawData(), daf));
        }
        return results;
    }

    public void resolveToFrame(Map<String, Int2DoubleOpenHashMap> a) {

    }


    public void sum() {

    }


}
