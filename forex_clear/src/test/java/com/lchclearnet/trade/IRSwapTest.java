package com.lchclearnet.trade;

import static org.junit.Assert.*;

import com.lchclearnet.calendar.BusinessCalendar;
import com.lchclearnet.calendar.BusinessDayConvention;
import com.lchclearnet.calendar.DayCountFraction;
import com.lchclearnet.calendar.SingleBusinessCalendar;
import com.lchclearnet.calendar.Tenor;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class IRSwapTest {

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

  @Test
  public void init() {

    IRSwap irs = new IRSwap(1e6, Tenor.parse("6M"),0.05,BusinessDayConvention.ModifiedFollowing,
        DayCountFraction.DCF_30_360,DayCountFraction.DCF_ACT_360,LocalDate.of(2011,11,14),
        LocalDate.of(2016,11,14), cal);


  }

}