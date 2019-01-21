package com.lchclearnet.jandas.utils;

import java.util.Arrays;
import org.ejml.data.DMatrixRMaj;

public class DoubleMatrixList extends DMatrixRMaj {

  private static double GROWTH_BASE = 2;

  public DoubleMatrixList(double vals[]) {
    double[] a = Arrays.copyOf(vals, dataSize(vals.length));
    double[] b = new double[1];
    DMatrixRMaj mat = new DMatrixRMaj(a);
  }

  private static double log(int x) {

    return Math.ceil(Math.log(x) / Math.log(GROWTH_BASE));
  }

  private static int dataSize(int x) {

    return (int) Math.ceil(Math.pow(GROWTH_BASE, log(x)));
  }

}
