package com.lchclearnet.calendar;

import org.junit.Before;
import org.junit.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertTrue;


public class BusinessCalendarTest {

    BusinessCalendar cal;
    LocalDate hol_dt;

    @Before
    public void setUp() throws Exception {

        Set<DayOfWeek> weekend = new HashSet<DayOfWeek>();
        weekend.add(DayOfWeek.SATURDAY);
        weekend.add(DayOfWeek.SUNDAY);

        hol_dt = LocalDate.of(2018, 1, 1);

        Set<LocalDate> holidays = new HashSet<LocalDate>();
        holidays.add(LocalDate.of(2018, 5, 1));
        holidays.add(LocalDate.of(2018, 6, 2));
        holidays.add(LocalDate.of(2018, 7, 3));
        holidays.add(LocalDate.of(2018, 1, 5));
        holidays.add(LocalDate.of(2018, 5, 6));
        holidays.add(hol_dt);

        cal = new SingleBusinessCalendar("test_cal", holidays, weekend);
    }

    @Test
    public void isBusinessDay() {
        assertTrue(!cal.isBusinessDay(hol_dt));
    }

}