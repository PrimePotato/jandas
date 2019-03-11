package io.github.primepotato.jandas.index.meta;

import io.github.primepotato.jandas.index.ColIndex;
import io.github.primepotato.jandas.index.generation.IndexGenerator;
import io.github.primepotato.jandas.index.impl.ObjectIndex;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.lang.reflect.Array;
import java.util.List;
import java.util.function.Function;

//import io.github.primepotato.jandas.index.impl.IntArrayListIndex;

public class MetaIndex {

    public int rowCount;
    public int colCount;
    public ObjectIndex<IntArrayList> index;
    public List<ColIndex> colIndices;

    public MetaIndex(List<ColIndex> cols) {
        this.colIndices = cols;
        rowCount = cols.get(0).size();
        colCount = cols.size();
        IntArrayList[] idx = new IntArrayList[rowCount];

        for (int i = 0; i < rowCount; i++) {
            IntArrayList rowIdx = new IntArrayList(2);
            for (ColIndex c : cols) {
                rowIdx.add(c.rowMap()[i]);
            }
            idx[i] = rowIdx;
        }

        index = new ObjectIndex<>(idx, IntArrayList.class);
    }

    public ObjectArrayList indexValue(int idx) {
        ObjectArrayList result = new ObjectArrayList();
        IntArrayList ial = IndexGenerator.indexValue(idx, IntArrayList.class);
        for (int i = 0; i < ial.size(); ++i) {
            ColIndex ci = this.colIndices.get(i);
            result.add(ci.indexValue(ial.getInt(i)));
        }
        return result;
    }

    public <T> Int2ObjectOpenHashMap<T> aggregate(T[] values, Function<T[], T> aggFunc, Class<T>
            cls) {

        Int2ObjectOpenHashMap<T> result = new Int2ObjectOpenHashMap<>();
        for (Int2ObjectArrayMap.Entry<IntArrayList> p : index.positions().int2ObjectEntrySet()) {
            IntArrayList rows = p.getValue();
            T[] objAry = (T[]) Array.newInstance(cls, rows.size());
            for (int i = 0; i < rows.size(); i++) {
                objAry[i] = values[rows.getInt(i)];
            }
            result.put(p.getIntKey(), aggFunc.apply(objAry));
        }
        return result;
    }

    public Object2DoubleOpenHashMap aggregateDouble(double[] values, Function<double[], Double> daf) {
        Object2DoubleOpenHashMap result = new Object2DoubleOpenHashMap();
        for (Int2ObjectArrayMap.Entry<IntArrayList> p : index.positions().int2ObjectEntrySet()) {
            IntArrayList rows = p.getValue();
            double[] objAry = new double[rows.size()];
            try {
                for (int i = 0; i < rows.size(); i++) {
                    objAry[i] = values[rows.getInt(i)];
                }
            } catch (Exception e) {
                System.out.println(1);
            }

            result.put(indexValue(p.getIntKey()), daf.apply(objAry));
        }
        return result;
    }

    public boolean unique() {

        return index.positions().size() == rowCount;
    }

}
