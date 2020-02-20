package io.github.primepotato.jandas.index.utils;

import io.github.primepotato.jandas.index.meta.JoinType;
import io.github.primepotato.jandas.index.meta.MetaIndex;
import io.github.primepotato.jandas.utils.Tuple;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import org.apache.commons.lang3.tuple.Pair;

public class IndexUtils {

    public static int[][] quickJoin(MetaIndex miLeft, MetaIndex miRight, JoinType jt) {


        if (!miLeft.unique()) {
            throw new RuntimeException("Left getIndex must be unique");
        }

        if (miLeft.colCount != miRight.colCount) {
            throw new RuntimeException("Indicies different dimension");
        }

        int[][] joinedRowMap = new int[3][miRight.rowCount];
        int[] rw = miRight.index.rowMap();

        Int2ObjectArrayMap<IntArrayList> pos = miLeft.index.positions();

        for (int i = 0; i < miRight.rowCount; i++) {
            if (pos.containsKey(rw[i])) {
                joinedRowMap[0][i] = rw[i];
                joinedRowMap[1][i] = pos.get(rw[i]).getInt(0);
                joinedRowMap[2][i] = i;
            }
            if (jt==JoinType.LEFT){
                joinedRowMap[0][i] = rw[i];
                joinedRowMap[1][i] = Integer.MIN_VALUE;
                joinedRowMap[2][i] = i;
            }
        }

        return joinedRowMap;
    }

    public static Pair<Boolean, int[][]> join(MetaIndex miLeft, MetaIndex miRight, JoinType jt) {
        if (miLeft.colCount != miRight.colCount) {
            throw new RuntimeException("Indicies different dimension");
        }
        if (miLeft.unique()) {
            return Pair.of(true, quickJoin(miLeft, miRight, jt));
        }
        if (miRight.unique()) {
            return Pair.of(false, quickJoin(miRight, miLeft, jt));
        }

        Int2ObjectArrayMap<IntArrayList> posLeft = miLeft.index.positions();
        Int2ObjectArrayMap<IntArrayList> posRight = miLeft.index.positions();

        int[][] joinedRowMap = new int[3][];

        for (int i : posLeft.keySet()) {
            IntArrayList leftIdxs = posLeft.get(i);
            IntArrayList rightIdxs = posRight.get(i);
            int[][] pairs = cartesianProduct(leftIdxs, rightIdxs);

        }

        return Pair.of(true, joinedRowMap);
    }

    public static int[][] cartesianProduct(IntArrayList a, IntArrayList b) {
        int[] ai = new int[a.size()];
        a.getElements(0, ai, 0, a.size());
        int[] bi = new int[b.size()];
        b.getElements(0, bi, 0, b.size());
        return cartesianProduct(ai, bi);
    }

    public static int[][] cartesianProduct(int[] a, int[] b) {
        int[][] output = new int[a.length * b.length][2];
        int count = 0;
        for (int i = 0; i < a.length; ++i) {
            for (int j = 0; j < b.length; ++j) {
                output[count][0] = a[i];
                output[count][1] = b[j];
                ++count;
            }
        }
        return output;
    }

}


