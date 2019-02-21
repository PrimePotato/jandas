package io.github.primepotato.jandas.column;

import io.github.primepotato.jandas.index.ColIndex;

import java.text.Normalizer;
import java.util.Set;
import java.util.stream.Collectors;
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

  public String cleanName() {

    return Normalizer.normalize(name, Normalizer.Form.NFD);
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

}
