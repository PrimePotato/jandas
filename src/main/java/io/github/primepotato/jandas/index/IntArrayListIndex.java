package io.github.primepotato.jandas.index;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;


public class IntArrayListIndex extends ColIndex {

  IntArrayList[] values;

  static Object2IntOpenHashMap<IntArrayList> IntArrayListIndex = new Object2IntOpenHashMap<>();

  static int nextIndex(IntArrayList val) {

    return IntArrayListIndex.computeIntIfAbsent(val, x -> IntArrayListIndex.size());
  }

  IntArrayListIndex(IntArrayList[] data) {

    values = data;
    int[] intMap = new int[data.length];
    for (int i = 0; i < data.length; i++) {
      intMap[i] = nextIndex(data[i]);
    }
    internalIntIndex = new IntIndex(intMap);

  }

}
