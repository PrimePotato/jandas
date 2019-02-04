package io.github.primepotato.jandas.index;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class IndexUtilsTest {

    IntArrayList a, b;

    @Before
    public void setUp() {
        a = new IntArrayList() {{
            add(1);
            add(2);
            add(3);
        }};
        b = new IntArrayList() {{
            add(4);
            add(5);
            add(6);
        }};
    }


    @Test
    public void cartesianProduct() {
        Object[] idxJoin = IndexUtils.cartesianProduct(a, b);
    }

}