package io.github.primepotato.jandas.dataframe;

import io.github.primepotato.jandas.utils.Tuple;
import org.junit.Test;

import static org.junit.Assert.*;

public class ColumnPositionTest {


    @Test
    public void init(){
        String[] k = {"Left", "Bob"};
        ColumnPosition<String[]> cp = new ColumnPosition<>(k, 1);
    }



}