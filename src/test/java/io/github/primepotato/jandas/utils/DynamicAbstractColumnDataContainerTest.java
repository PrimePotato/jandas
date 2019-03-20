package io.github.primepotato.jandas.utils;


import io.github.primepotato.jandas.io.csv.containers.DynamicAbstractColumnDataContainer;
import io.github.primepotato.jandas.io.parsers.DoubleParser;
import io.github.primepotato.jandas.io.parsers.IntParser;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class DynamicAbstractColumnDataContainerTest {

  @Test
  public void add() {
    DynamicAbstractColumnDataContainer pcd = new DynamicAbstractColumnDataContainer("BOB");
    pcd.add("1");
    pcd.add("2");
    pcd.add("3");
    assertTrue(pcd.parser instanceof IntParser);
    pcd.add(".1");
    pcd.add(".2");
    pcd.add(".3");
    assertTrue(pcd.parser instanceof DoubleParser);
    assertTrue(pcd.data.size()==6);
    pcd.add("12/10/2001");
    pcd.add("12/10/2006");
    pcd.add("12/10/2005");
    assertTrue(pcd.data.size()==9);
  }
}