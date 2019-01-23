package com.lchclearnet.jandas.column;

import com.lchclearnet.jandas.index.ColIndex;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.time.LocalTime;
import java.util.AbstractCollection;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.ClassUtils;
//import org.apache.commons.lang.ClassUtils;

public abstract class AbstractColumn implements Column {

  public ColIndex index;
  public String name;
  public Class dataType;
  public Boolean indexed;

  public abstract void rebuildIndex();

  public boolean unique() {

    return index.unique();
  }

  public Set uniqueSet() {

    return index.positions()
        .values()
        .stream()
        .map(x -> getObject(x.getInt(0)))
        .collect(Collectors.toSet());
  }

  public String name() {

    return name;
  }

  public ColIndex index() {
    if (index==null){
      rebuildIndex();
      return index();
    } else {
      return index;
    }
  }

  public <T> T firstValue() {

    int idx = 0;
    T t = null;
    while (t == null) {
      t = getObject(idx);
      idx++;
    }
    return t;
  }

  public void implyType(){



  }

}
