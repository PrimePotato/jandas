package com.lchclearnet.jandas.index;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;

public class IndexUtils {

  public static int[][] quickJoin(MetaIndex miLeft, MetaIndex miRight) {


    if (!miLeft.unique()) {
      throw new RuntimeException("Left index must be unique");
    }

    if (miLeft.colCount != miRight.colCount) {
      throw new RuntimeException("Indicies different dimension");
    }

    int[][] joinedRowMap = new int[3][miRight.rowCount];

    int[] rw = miRight.index.rowMap();
    Int2ObjectArrayMap<IntArrayList> pos = miLeft.index.positions();
    for (int i = 0; i < miRight.rowCount; i++) {
      joinedRowMap[0][i] = rw[i];
      joinedRowMap[1][i] = pos.get(rw[i]).getInt(0);
      joinedRowMap[2][i] = i;
    }

    return joinedRowMap;
  }


}


