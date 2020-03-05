package io.github.primepotato.jandas.utils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DoubleAggregateFuncTest {

    double[] values;

    @Before
    public void setUp(){
        values = new double[]{1.,2.,3.,4.,5.,5.,6.};
    }

    @Test
    public void min(){
        Assert.assertEquals(1., DoubleAggregateFunc.MIN.apply(values), 1e-15);
    }

    @Test
    public void max(){
        Assert.assertEquals(6., DoubleAggregateFunc.MAX.apply(values), 1e-15);
    }

    @Test
    public void mean(){
        Assert.assertEquals(3.7142857142857144, DoubleAggregateFunc.MEAN.apply(values), 1e-15);
    }

    @Test
    public void sum(){
        Assert.assertEquals(26., DoubleAggregateFunc.SUM.apply(values), 1e-15);
    }

    @Test
    public void count(){
        Assert.assertEquals(7., DoubleAggregateFunc.COUNT.apply(values), 1e-15);
    }

}