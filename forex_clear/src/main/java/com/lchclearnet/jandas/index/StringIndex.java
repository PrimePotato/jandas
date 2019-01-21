package com.lchclearnet.jandas.index;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

public class StringIndex extends ColIndex{

  public static Object2IntOpenHashMap<String> stringIndex = new Object2IntOpenHashMap<>();

  public StringIndex(String[] data) {

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
