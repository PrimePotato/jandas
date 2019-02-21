package io.github.primepotato.jandas.utils;

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
  }, COUNT {
    public double apply(double[] vals) {

      return vals.length;
    }
  }, MEAN {
    public double apply(double[] vals) {

      return SUM.apply(vals)/COUNT.apply(vals);
    }
  }

}
