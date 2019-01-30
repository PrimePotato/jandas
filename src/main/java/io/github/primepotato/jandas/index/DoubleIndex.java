package io.github.primepotato.jandas.index;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

public class DoubleIndex extends ColIndex {

  double[] values;

  static Object2IntOpenHashMap<Double> doubleIndex = new Object2IntOpenHashMap<>();

  static int nextIndex(double val) {

    return doubleIndex.computeIntIfAbsent(val, x -> doubleIndex.size());
  }

  public DoubleIndex(double[] data) {

    values = data;
    int[] intMap = new int[data.length];
    for (int i = 0; i < data.length; i++) {
      intMap[i] = nextIndex(data[i]);
    }
    internalIntIndex = new IntIndex(intMap);

  }

}
