package com.lchclearnet.jandas.index;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;

public class IndexUtils {

  public static int[][] quickJoin(MetaIndex miLeft, MetaIndex miRight) {

    if (miLeft.colCount != miRight.colCount) {
      throw new RuntimeException("Indicies different dimension");
    }

    if (!miLeft.unique() && !miRight.unique()) {
      throw new RuntimeException("At least one index must be unique");
    }

    if (miLeft.index.positions().keySet().size() != miRight.index.positions().keySet().size()) {
      throw new RuntimeException("Unique elements mismatch");
    }
    ;

    MetaIndex miUnique;
    MetaIndex miMany;
    int leftCol = 2;
    int rightCol = 1;

    if (miLeft.unique()) {
      miUnique = miLeft;
      miMany = miRight;
    } else {
      leftCol = 1;
      rightCol = 2;
      miUnique = miRight;
      miMany = miLeft;
    }

    int[][] joinedRowMap = new int[3][miMany.rowCount];

    int[] rw = miMany.index.rowMap();
    Int2ObjectArrayMap<IntArrayList> pos = miUnique.index.positions();
    for (int i = 0; i < miMany.rowCount; i++) {
      joinedRowMap[0][i] = rw[i];
      joinedRowMap[leftCol][i] = i;
      joinedRowMap[rightCol][i] = pos.get(rw[i]).getInt(0);
    }

    return joinedRowMap;
  }

}


