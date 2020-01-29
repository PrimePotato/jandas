package io.github.primepotato.jandas.utils;

import java.util.function.Consumer;
import java.util.function.Function;

public class PerformanceTester {

    Consumer<Void> consumer;
    int n;

    public PerformanceTester(Consumer consumer){
        this(consumer, 1000);
    }

    public PerformanceTester(Consumer consumer, int n) {
        this.consumer = consumer;
        this.n = n;
    }

    public double run(){
        long t0 = System.currentTimeMillis();
        for (int i=0; i<n; ++i){
            consumer.accept(null);
        }
        long t1 = System.currentTimeMillis();
        return (double)(t1-t0)/((double)n);
    }

}
