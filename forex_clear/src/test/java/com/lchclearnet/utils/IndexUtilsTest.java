//package com.lchclearnet.utils;
//
//import com.lchclearnet.jandas.index.ColumnIndex;
//import com.lchclearnet.jandas.index.IndexUtils;
//import com.lchclearnet.jandas.index.MetaIndex;
//import it.unimi.dsi.fastutil.ints.IntArrayList;
//import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
//import java.time.LocalDate;
//import java.util.Arrays;
//import org.junit.Before;
//import org.junit.Test;
//
//public class IndexUtilsTest {
//
//  ColumnIndex<Double> c1;
//  ColumnIndex<String> c2;
//
//  ColumnIndex<String> cp;
//  ColumnIndex<LocalDate> ce;
//
//  MetaIndex miLeft;
//  MetaIndex miRight;
//
//  @Before
//  public void setUp() {
//
//    Double[] dbl = {.1, .2, .3, .1, .4, .3};
//    c1 = new ColumnIndex<>(dbl);
//    String[] str = {"a", "b", "c", "a", "b", "c",};
//    c2 = new ColumnIndex<>(str);
//
//    String[] ccyPair = {
//        "EURUSD", "EURUSD", "EURUSD", "GBPUSD"
//    };
//    LocalDate[] expiry = {
//        LocalDate.of(2019, 1, 1),
//        LocalDate.of(2019, 3, 1),
//        LocalDate.of(2019, 3, 1),
//        LocalDate.of(2019, 3, 1),
//    };
//
//    cp = new ColumnIndex<>(ccyPair);
//    ce = new ColumnIndex<>(expiry);
//    miLeft = new MetaIndex(Arrays.asList(cp, ce));
//
//    String[] ccyPair2 = {
//        "EURUSD", "EURUSD", "GBPUSD"
//    };
//    LocalDate[] expiry2 = {
//        LocalDate.of(2019, 1, 1),
//        LocalDate.of(2019, 3, 1),
//        LocalDate.of(2019, 3, 1),
//    };
//
//
//    miRight = new MetaIndex(Arrays.asList(new ColumnIndex<>(ccyPair2), new ColumnIndex<>
//        (expiry2)));
//
//
//
//  }
//
//  @Test
//  public void intArrayListExample() {
//
//    int[] a = {1, 2};
//    int[] b = {1, 2};
//    System.out.println(a == b);
//    System.out.println(Arrays.equals(a, b));
//
//    IntArrayList aa = new IntArrayList(a);
//    IntArrayList bb = new IntArrayList(b);
//    System.out.println(aa == bb);
//
//    Object2IntOpenHashMap<IntArrayList> dic = new Object2IntOpenHashMap();
//    dic.put(aa, 1);
//    dic.put(bb, 2);
//
//    System.out.println(dic);
//
//    Object2IntOpenHashMap<int[]> dd = new Object2IntOpenHashMap();
//    dd.put(a, 1);
//    dd.put(b, 2);
//
//    System.out.println(dd);
//
//  }
//
//  @Test
//  public void createMultiIndex() {
//
//    int[] c1rm = c1.rowMap;
//    int[] c2rm = c2.rowMap;
//
//    IntArrayList[] idx = new IntArrayList[c1.rowMap.length];
//    for (int i = 0; i < c1rm.length; i++) {
//      IntArrayList row = new IntArrayList(2);
//      row.add(c1rm[i]);
//      row.add(c2rm[i]);
//      idx[i] = row;
//    }
//
//    ColumnIndex<IntArrayList> cm = new ColumnIndex<>(idx);
//
//    System.out.println(cm.groups);
//  }
//
//  @Test
//  public void quickJoin() {
//
//    IndexUtils.quickJoin(miLeft, miRight);
//  }
//}