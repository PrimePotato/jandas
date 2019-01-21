package com.lchclearnet.calendar;

import java.util.function.Function;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.junit.Before;
import org.junit.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;
import scala.tools.nsc.backend.icode.Members.Local;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class DateUtilsTest {

  BusinessCalendar cal;
  Set<BusinessCalendar> cals;
  Set<LocalDate> holidays;
  Set<DayOfWeek> weekend;
  private Tenor t1m, t2w, t3y;
  private LocalDate dt, hol_dt;

  @Before
  public void setUp() throws Exception {

    t1m = Tenor.parse("1M");
    t2w = Tenor.parse("2W");
    t3y = Tenor.parse("3Y");
    dt = LocalDate.of(2018, 1, 5);

    weekend = new HashSet<DayOfWeek>();
    weekend.add(DayOfWeek.SATURDAY);
    weekend.add(DayOfWeek.SUNDAY);

    hol_dt = LocalDate.of(2018, 1, 1);

    holidays = new HashSet<LocalDate>();
    holidays.add(LocalDate.of(2018, 5, 1));
    holidays.add(LocalDate.of(2018, 6, 2));
    holidays.add(LocalDate.of(2018, 7, 3));
    holidays.add(LocalDate.of(2018, 1, 5));
    holidays.add(LocalDate.of(2018, 5, 6));
    holidays.add(hol_dt);

    cal = new SingleBusinessCalendar("test_cal", holidays, weekend);
    cals = new HashSet<>();
    cals.add(cal);

  }

  @Test
  public void resolveTenor() {

    System.out.println(DateUtils.resolveTenor(dt, t2w, cals));
    assertEquals(DateUtils.resolveTenor(dt, t2w, cals), LocalDate.of(2018, 1, 19));

    System.out.println(DateUtils.resolveTenor(dt, t1m, cals));
    assertEquals(DateUtils.resolveTenor(dt, t1m, cals), LocalDate.of(2018, 2, 5));

    System.out.println(DateUtils.resolveTenor(dt, t3y, cals));
    assertEquals(DateUtils.resolveTenor(dt, t3y, cals), LocalDate.of(2021, 1, 5));
  }

  @Test
  public void weekendBusinessDay() {

    Set<DayOfWeek> wkd = new HashSet<DayOfWeek>();
    wkd.add(DayOfWeek.MONDAY);
    wkd.add(DayOfWeek.TUESDAY);
    LocalDate dt = LocalDate.of(2015, 1, 5);
    System.out.println(dt.getDayOfWeek());
    BusinessCalendar test_cal = new SingleBusinessCalendar("test_cal", holidays, wkd);
    assertFalse(test_cal.isBusinessDay(dt));
  }

  @Test
  public void offsetBusinessDay() {

    System.out.println(DateUtils.offsetBusinessDay(hol_dt, cal));
    assertEquals(DateUtils.offsetBusinessDay(hol_dt, cal), LocalDate.of(2018, 1, 2));
  }

  @Test
  public void businessDayTenor() {

    Tenor t1b = Tenor.parse("1B");
    LocalDate dt_before = hol_dt.minus(1, ChronoUnit.DAYS);
    LocalDate dt_after = DateUtils.resolveTenor(dt_before, t1b, cal);
    System.out.println(dt_after);
    assertEquals(dt_after, LocalDate.of(2018, 1, 2));
  }

  @Test
  public void leapDaysBetween() {

    LocalDate dt_before = LocalDate.of(2012, 1, 1);
    LocalDate dt_after = LocalDate.of(2018, 1, 1);

    System.out.println(DateUtils.leapDaysBetween(dt_before, dt_after));
  }

  @Test
  public void tradingTimeInterp() {

    LocalDate start = LocalDate.of(2012, 1, 1);
    LocalDate end = LocalDate.of(2013, 12, 29);

    Function<LocalDate, Double> it = DateUtils.tradingTimeInterp(start, end, cal);
    System.out.println(it.apply(end));
  }

}