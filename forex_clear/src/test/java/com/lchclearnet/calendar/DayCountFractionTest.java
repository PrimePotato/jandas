package com.lchclearnet.calendar;

import org.junit.Test;

import java.time.LocalDate;

public class DayCountFractionTest {

    @Test
    public void calc_30_60() {

        LocalDate st = LocalDate.of(2018, 1, 1);
        LocalDate ed = LocalDate.of(2018, 2, 5);
        double dcf = DayCountFraction.DCF_30_360.calc(st, ed);
        System.out.println(dcf);
    }

    @Test
    public void calc_ACT_ACT() {

        LocalDate st = LocalDate.of(2018, 1, 1);
        LocalDate ed = LocalDate.of(2018, 2, 5);
        double dcf = DayCountFraction.DCF_ACT_ACT.calc(st, ed);
        System.out.println(dcf);
    }

    @Test
    public void calc_ACT_360() {

        LocalDate st = LocalDate.of(2018, 1, 1);
        LocalDate ed = LocalDate.of(2018, 2, 5);
        double dcf = DayCountFraction.DCF_ACT_360.calc(st, ed);
        System.out.println(dcf);
    }

    @Test
    public void calc_ACT_365F() {

        LocalDate st = LocalDate.of(2018, 1, 1);
        LocalDate ed = LocalDate.of(2018, 2, 5);
        double dcf = DayCountFraction.DCF_ACT_365F.calc(st, ed);
        System.out.println(dcf);
    }

}