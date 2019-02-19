package io.github.primepotato.jandas.index.generation;

import it.unimi.dsi.fastutil.ints.Int2IntArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;

public class IntIndex {

  public Int2ObjectArrayMap<IntArrayList> positions;
  public Int2IntArrayMap indexOf;
  public int[] values, rowMap;

  public IntIndex(int[] data) {

    values = data;
    indexOf = new Int2IntArrayMap();
    positions = new Int2ObjectArrayMap<>();
    rowMap = new int[data.length];
    indexOf.defaultReturnValue(-1);

    int d;
    int idx;
    IntArrayList pos;

    for (int i = 0; i < data.length; i++) {
      d = data[i];
      if (!indexOf.containsKey(d)) {
        idx = IndexGenerator.nextIndex(d);
        pos = positions.get(idx);
        if (pos == null) {
          pos = new IntArrayList();
          positions.put(idx, pos);
        }
        indexOf.put(d, idx);
      } else {
        idx = indexOf.get(d);
        pos = positions.get(idx);
      }
      pos.add(i);
      rowMap[i] = idx;
    }

  }

  public int get(int val) {

    return indexOf.get(val);
  }

  public int[] rowMap() {

    return rowMap;
  }

  public int size() {

    return values.length;
  }

  public Int2ObjectArrayMap<IntArrayList> positions() {

    return positions;
  }

  public void clear(){
    positions.clear();
    indexOf.clear();

    values = new int[0];
    rowMap = new int[0];
  }

}
