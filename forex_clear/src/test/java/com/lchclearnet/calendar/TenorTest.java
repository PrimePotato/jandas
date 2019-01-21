package com.lchclearnet.calendar;

import org.junit.Test;

import java.time.temporal.ChronoUnit;

import static org.junit.Assert.assertEquals;


public class TenorTest {

    @Test
    public void init() {
        Tenor t1 = Tenor.parse("1D");
        Tenor t2 = Tenor.parse("2W");
        Tenor t3 = Tenor.parse("3M");
        Tenor t4 = Tenor.parse("6B");
        Tenor t5 = Tenor.parse("10Y");

        assertEquals(t1.value(), 1);
        assertEquals(t1.unit(), ChronoUnit.DAYS);

        assertEquals(t2.value(), 2);
        assertEquals(t2.unit(), ChronoUnit.WEEKS);

        assertEquals(t3.value(), 3);
        assertEquals(t3.unit(), ChronoUnit.MONTHS);

        assertEquals(t4.value(), 6);
        assertEquals(t4.unit(), ChronoUnit.DAYS);

        assertEquals(t5.value(), 10);
        assertEquals(t5.unit(), ChronoUnit.YEARS);
    }


}