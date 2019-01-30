package io.github.primepotato.jandas.index;//package com.lchclearnet.jandas.index;
//
//import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
//import it.unimi.dsi.fastutil.ints.IntArrayList;
//import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
//import it.unimi.dsi.fastutil.objects.ObjectArrayList;
//
//public class ColumnIndex<T> {
//
//  T[] vector;
//
//  public Int2ObjectArrayMap<IntArrayList> positions;
//  public Int2ObjectArrayMap<ObjectArrayList<T>> groups;
//
//  public Object2IntArrayMap<T> indexOf;
//  public int[] rowMap;
//
//  public ColumnIndex(T[] data) {
//
//    vector = data;
//    indexOf = new Object2IntArrayMap<>();
//    positions = new Int2ObjectArrayMap<>();
//    groups = new Int2ObjectArrayMap<>();
//    rowMap = new int[data.length];
//    indexOf.defaultReturnValue(-1);
//
//    T d;
//    int idx;
//    IntArrayList pos;
//    ObjectArrayList<T> grp;
//    for (int i = 0; i < data.length; i++) {
//      d = data[i];
//      if (!indexOf.containsKey(d)) {
//        idx = IndexGeneration.getObject(d);
//        pos = positions.getObject(idx);
//        grp = groups.getObject(idx);
//        if (pos == null) {
//          pos = new IntArrayList();
//          grp = new ObjectArrayList<>();
//          positions.put(idx, pos);
//          groups.put(idx, grp);
//        }
//        indexOf.put(d, idx);
//      } else {
//        idx = indexOf.getInt(d);
//        pos = positions.getObject(idx);
//        grp = groups.getObject(idx);
//      }
//      grp.add(d);
//      pos.add(i);
//      rowMap[i] = idx;
//    }
//
//  }
//
//  public IntArrayList rowsAtIndex(int iid) {
//
//    return positions.getObject(iid);
//  }
//
//  public ObjectArrayList<T> valuesAtIndex(int iid) {
//
//    return groups.getObject(iid);
//  }
//
//  public int getObject(T val) {
//
//    return indexOf.getInt(val);
//  }
//
//  public int size() {
//
//    return vector.length;
//  }
//
//
//}
