package io.github.primepotato.jandas.utils;

import org.junit.Before;
import org.junit.Test;

import java.util.Random;
import java.util.function.Consumer;

import static org.junit.Assert.*;

public class PerformanceTesterTest {

    PerformanceTester pt;

    Consumer<Void> randGen = new Consumer<Void>() {
        @Override
        public void accept(Void aVoid) {
            for (int i=0; i<1000; ++i){
                Math.random();
            }
        }
    };

    @Before
    public void setUp() throws Exception {

        pt = new PerformanceTester(randGen,10000);
    }

    @Test
    public void run() {
        System.out.println(pt.run());
    }
}