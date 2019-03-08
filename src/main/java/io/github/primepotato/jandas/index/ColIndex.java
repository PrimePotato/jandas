package io.github.primepotato.jandas.index;

import io.github.primepotato.jandas.index.generation.IndexGenerator;
import io.github.primepotato.jandas.index.generation.IntIndex;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;

public abstract class ColIndex {

  public IntIndex internalIntIndex;

  public int[] rowMap() {

    return internalIntIndex.rowMap();
  }

  public boolean unique() {

    return internalIntIndex.positions().keySet().size() == size();
  }

  public int size() {

    return internalIntIndex.values.length;
  }

  public Int2ObjectArrayMap<IntArrayList> positions() {

    return internalIntIndex.positions();
  }

  public abstract Class elementClass();

  public <T> T indexValue(int i){
    return IndexGenerator.indexValue(i, elementClass());
  }

}

