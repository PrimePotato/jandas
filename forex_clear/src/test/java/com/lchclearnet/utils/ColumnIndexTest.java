//package com.lchclearnet.utils;
//
//import com.lchclearnet.jandas.index.ColumnIndex;
//import java.util.Arrays;
//import org.junit.BeforeClass;
//import org.junit.Test;
//
//public class ColumnIndexTest {
//
//  static ColumnIndex<Double> ci;
//
//  @BeforeClass
//  public static void setUp() throws Exception {
//    int n = 100;
//    Double[] data = new Double[n];
//    Double[] data1 = Arrays.stream(data).map(x->Math.random()).toArray(Double[]::new);
//    ci = new ColumnIndex<>(data1);
//    System.out.println(ci.groups);
//    System.out.println(ci.position);
//    System.out.println(ci.indexOf);
//  }
//
//  @Test
//  public void size() {
//
//    ci.size();
//  }
//
//  @Test
//  public void rowsAtIndex() {
//
//    System.out.println(ci.rowsAtIndex(2));
//    System.out.println(ci.rowsAtIndex(3));
//    System.out.println(ci.rowsAtIndex(3));
//    System.out.println(ci.rowsAtIndex(3));
//    System.out.println(ci.rowsAtIndex(3));
//    System.out.println(ci.rowsAtIndex(3));
//    System.out.println(ci.rowsAtIndex(3));
//    System.out.println(ci.rowsAtIndex(3));
//    System.out.println(ci.rowsAtIndex(3));
//    System.out.println(ci.rowsAtIndex(3));
//    System.out.println(ci.rowsAtIndex(4));
//
//  }
//
//  @Test
//  public void getObject() {
//
//    System.out.println(ci.getObject(1.3));
//    System.out.println(ci.getObject(1.45));
//    System.out.println(ci.getObject(1.1351365));
//  }
//
//  @Test
//  public void valuesAtIndex() {
//
//    System.out.println(ci.valuesAtIndex(0));
//    System.out.println(ci.valuesAtIndex(2));
//    System.out.println(ci.valuesAtIndex(1));
//  }
//
//
//
//}