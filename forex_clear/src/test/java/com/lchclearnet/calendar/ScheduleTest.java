package com.lchclearnet.calendar;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;

public class ScheduleTest {

  BusinessCalendar cal;
  BusinessDayConvention bdc;
  Set<BusinessCalendar> cals;
  private Set<LocalDate> holidays;
  private Set<DayOfWeek> weekend;

  @Before
  public void setUp() throws Exception {

    weekend = new HashSet<DayOfWeek>();
    weekend.add(DayOfWeek.SATURDAY);
    weekend.add(DayOfWeek.SUNDAY);

    holidays = new HashSet<LocalDate>();

    cal = new SingleBusinessCalendar("test_cal", holidays, weekend);

  }

//  @Test
//  public void init() {
//
//    Schedule sch =
//        new Schedule(Tenor.parse("-6M"), LocalDate.of(2011, 11, 14), LocalDate.of(2016, 11, 14),
//            cal, BusinessDayConvention.ModifiedFollowing);
//
//    System.out.println(sch.start_dates);
//    System.out.println(sch.end_dates);
//  }

}