package com.lchclearnet.jandas.index;

import java.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

public class DateIndexTest {

  @Before
  public void setUp() {
    LocalDate[] data = {
        LocalDate.of(2019,3,3),
        LocalDate.of(2019,3,3),
        LocalDate.of(2018,3,3),
        LocalDate.of(2019,1,3)
    };
    DateIndex di = new DateIndex(data);
  }

  @Test
  public void nextIndex() {

  }

  @Test
  public void rowMap() {

  }

  @Test
  public void size() {

  }

  @Test
  public void positions() {

  }

  @Test
  public void groups() {

  }
}