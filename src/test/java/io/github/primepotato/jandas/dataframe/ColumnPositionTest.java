package io.github.primepotato.jandas.dataframe;

import org.junit.Test;

public class ColumnPositionTest {


    @Test
    public void init(){
        String[] k = {"Left", "Bob"};
        ColumnPosition<String[]> cp = new ColumnPosition<>(k, 1);
    }



}