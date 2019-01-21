package com.lchclearnet.jandas.index;

import it.unimi.dsi.fastutil.ints.Int2IntArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;

public class IntIndex {

  Int2ObjectArrayMap<IntArrayList> positions;
//  Int2ObjectArrayMap<IntArrayList> groups;
  Int2IntArrayMap indexOf;

  int[] values, rowMap;

  IntIndex(int[] data) {

    values = data;
    indexOf = new Int2IntArrayMap();
    positions = new Int2ObjectArrayMap<>();
//    groups = new Int2ObjectArrayMap<>();
    rowMap = new int[data.length];
    indexOf.defaultReturnValue(-1);

    int d;
    int idx;
    IntArrayList pos;
    IntArrayList grp;

    for (int i = 0; i < data.length; i++) {
      d = data[i];
      if (!indexOf.containsKey(d)) {
        idx = nextIndex(d);
        pos = positions.get(idx);
//        grp = groups.get(idx);
        if (pos == null) {
          pos = new IntArrayList();
          grp = new IntArrayList();
          positions.put(idx, pos);
//          groups.put(idx, grp);
        }
        indexOf.put(d, idx);
      } else {
        idx = indexOf.get(d);
        pos = positions.get(idx);
//        grp = groups.get(idx);
      }
//      grp.add(d);
      pos.add(i);
      rowMap[i] = idx;
    }

  }

  static int nextIndex(int val) {

    return val;
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

//  public Int2ObjectArrayMap<IntArrayList> groups() {
//
//    return groups;
//  }

  public void clear(){
    positions.clear();
//    groups.clear();
    indexOf.clear();

    values = new int[0];
    rowMap = new int[0];
  }

}
