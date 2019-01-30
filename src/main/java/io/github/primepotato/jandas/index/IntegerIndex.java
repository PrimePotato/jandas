package io.github.primepotato.jandas.index;

public class IntegerIndex extends ColIndex {

  int[] values;

  static int nextIndex(int val) {

    return val;
  }

  public IntegerIndex(int[] data) {

    values = data;
    internalIntIndex = new IntIndex(data);

  }

}
