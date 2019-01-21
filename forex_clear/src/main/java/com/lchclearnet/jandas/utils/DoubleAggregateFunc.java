package com.lchclearnet.jandas.utils;

import java.util.Arrays;

public enum DoubleAggregateFunc implements DoubleFunc {
  SUM {
    public double apply(double[] vals) {

      return Arrays.stream(vals).sum();
    }
  }, MAX {
    public double apply(double[] vals) {

      return Arrays.stream(vals).max().getAsDouble();
    }
  }, MIN {
    public double apply(double[] vals) {

      return Arrays.stream(vals).min().getAsDouble();
    }
  },

}
