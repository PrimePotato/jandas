package com.lchclearnet.jandas.index;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

public class ObjectIndex<T> extends ColIndex{
  //TODO: do
  String[] values;

  static Object2IntOpenHashMap<String> stringIndex = new Object2IntOpenHashMap<>();

  public ObjectIndex(String[] data) {

    values = data;
    int[] intMap = new int[data.length];
    for (int i = 0; i < data.length; i++) {
      intMap[i] = nextIndex(data[i]);
    }
    internalIntIndex = new IntIndex(intMap);

  }

  static int nextIndex(String val) {

    return stringIndex.computeIntIfAbsent(val, x -> stringIndex.size());
  }

}
