package com.lchclearnet.jandas.index;

import com.lchclearnet.jandas.utils.DoubleAggregateFunc;
import it.unimi.dsi.fastutil.ints.Int2DoubleOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import java.lang.reflect.Array;
import java.util.List;
import java.util.function.Function;
import javax.sound.midi.SysexMessage;

public class MetaIndex {

  public int rowCount;
  public int colCount;
  public IntArrayListIndex index;

  public MetaIndex(List<ColIndex> cols) {

    rowCount = cols.get(0).size();
    colCount = cols.size();
    IntArrayList[] idx = new IntArrayList[rowCount];

    for (int i = 0; i < rowCount; i++) {
      IntArrayList rowIdx = new IntArrayList(2);
      for (ColIndex c : cols) {
        rowIdx.add(c.rowMap()[i]);
      }
      idx[i] = rowIdx;
    }

    index = new IntArrayListIndex(idx);
  }

  public <T> Int2ObjectOpenHashMap<T> aggregate(T[] values, Function<T[], T> aggFunc, Class<T>
      cls) {

    Int2ObjectOpenHashMap<T> result = new Int2ObjectOpenHashMap<>();
    for (Int2ObjectArrayMap.Entry<IntArrayList> p : index.positions().int2ObjectEntrySet()) {
      IntArrayList rows = p.getValue();
      T[] objAry = (T[]) Array.newInstance(cls, rows.size());
      for (int i = 0; i < rows.size(); i++) {
        objAry[i] = values[rows.getInt(i)];
      }
      result.put(p.getIntKey(), aggFunc.apply(objAry));
    }
    return result;
  }

  public Int2DoubleOpenHashMap aggregateDouble(double[] values, DoubleAggregateFunc daf)  {
    Int2DoubleOpenHashMap result = new Int2DoubleOpenHashMap();
    for (Int2ObjectArrayMap.Entry<IntArrayList> p : index.positions().int2ObjectEntrySet()) {
      IntArrayList rows = p.getValue();
      double[] objAry = new double[rows.size()];
      try {
        for (int i = 0; i < rows.size(); i++) {
          objAry[i] = values[rows.getInt(i)];
        }
      } catch (Exception e){
        System.out.println(1);
      }

      result.put(p.getIntKey(), daf.apply(objAry));
    }
    return result;
  }

  public boolean unique() {

    return index.positions().size() == rowCount;
  }

}
