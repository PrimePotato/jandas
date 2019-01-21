package com.lchclearnet.utils;

import org.ejml.simple.SimpleMatrix;
import org.junit.Test;

public class EjmlTest {

  @Test
  public void add() {

    double[] a, b;
    a = new double[]{1., 2., 3.};
    b = new double[]{2., 5., 8.};

    SimpleMatrix A = new SimpleMatrix(new double[][] {a});
    SimpleMatrix B = new SimpleMatrix(new double[][] {b});

    System.out.println(A.plus(B));
    System.out.println(A.equals(B));

  }


}
