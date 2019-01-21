package com.lchclearnet.utils;



import com.lchclearnet.jandas.index.DoubleIndex;
import com.lchclearnet.jandas.index.MetaIndex;
import com.lchclearnet.jandas.index.StringIndex;
import java.util.Arrays;

import java.util.function.Function;
import org.junit.Before;
import org.junit.Test;

public class MetaIndexTest {

  DoubleIndex c1;
  StringIndex c2;
  Double[] vals = {.1, .01, .21, .2, 14., .4, .6, .7, 9.};
  MetaIndex mi;

  @Before
  public void setUp() throws Exception {

    double[] dbl = {1., 2., 3., 1., 2., 3., 4.};
    String[] str = {"a", "b", "c", "a", "b", "c", "a"};

    c1 = new DoubleIndex(dbl);
    c2 = new StringIndex(str);
    mi = new MetaIndex(Arrays.asList(c1, c2));

  }

  @Test
  public void grouping() {

    System.out.println(mi.index.positions());

  }

  @Test
  public void aggregate() {

    Function<Double[], Double> aggFunc = (ary) -> {
      Double total = 0.;
      System.out.println(Arrays.toString(ary));
      for (Double item : ary) {
        total += item;
      }
      return total;
    };

    System.out.println(mi.aggregate(vals, aggFunc, Double.class));
  }

}