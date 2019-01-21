package com.lchclearnet.utils;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Arrays;
import org.junit.Test;

public class ObjectArrayListTest {

  @Test
  public void typeCheck(){
    String[] a = new String[5];
    a[0] = "bob";
    a[1] = "bob";
    a[2] = null;
    a[3] = "bob";
    a[4] = null;

    ObjectArrayList<Object> oa = ObjectArrayList.wrap(a);
    System.out.println(Arrays.toString(oa.elements()));

  }

}
