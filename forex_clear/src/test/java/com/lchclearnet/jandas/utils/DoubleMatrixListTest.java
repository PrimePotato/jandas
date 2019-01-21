package com.lchclearnet.jandas.utils;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class DoubleMatrixListTest {

  @Test
  public void init() {
    double[] a = {.1,.2,.3,.5};
    DoubleMatrixList dml = new DoubleMatrixList(a);
  }

}