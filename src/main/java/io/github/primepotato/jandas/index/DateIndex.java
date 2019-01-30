package io.github.primepotato.jandas.index;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

import java.time.LocalDate;

public class DateIndex extends ColIndex {

  LocalDate[] values;

  static Object2IntOpenHashMap<LocalDate> dateIndex = new Object2IntOpenHashMap<>();

  static int nextIndex(LocalDate val) {

    return dateIndex.computeIntIfAbsent(val, x -> dateIndex.size());
  }

  DateIndex(LocalDate[] data) {

    values = data;
    int[] intMap = new int[data.length];
    for (int i = 0; i < data.length; i++) {
      intMap[i] = nextIndex(data[i]);
    }
    internalIntIndex = new IntIndex(intMap);

  }


}
