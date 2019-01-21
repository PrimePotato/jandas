package com.lchclearnet.calendar;

import java.time.LocalDate;
import java.time.Year;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.interpolation.LinearInterpolator;

import static java.time.temporal.ChronoUnit.DAYS;

public class DateUtils {

  public static LocalDate resolveTenor(LocalDate dt, Tenor t, BusinessCalendar cal) {

    Set<BusinessCalendar> cs = new HashSet<>();
    cs.add(cal);
    return resolveTenor(dt, t, cs);
  }

  public static LocalDate resolveTenor(LocalDate dt, Tenor t, BusinessCalendar... cals) {

    return resolveTenor(dt, t,
        (Iterable<BusinessCalendar>) Arrays.stream(cals).collect(Collectors.toList()));
  }

  public static LocalDate resolveTenor(LocalDate dt, Tenor t, Iterable<BusinessCalendar> cals) {

    if (t.businessDayOffset()) {
      if (t.unit() != ChronoUnit.DAYS) {
        throw new RuntimeException("Invalid business days tenor %s".format(t.toString()));
      }
      LocalDate dt_new = null;
      for (int i = 0; i < t.value(); ++i) {
        dt_new = dt.plus(1, t.unit());
        for (BusinessCalendar c : cals) {
          dt_new = offsetBusinessDay(dt_new, c);
        }
      }
      return dt_new;
    } else {
      return dt.plus(t.value(), t.unit());
    }
  }

  public static LocalDate offsetBusinessDay(LocalDate dt, BusinessCalendar cal) {

    return offsetBusinessDay(dt, cal, 1, "F");
  }

  public static LocalDate offsetBusinessDay(LocalDate dt, BusinessCalendar cal, Integer days,
      String rule) {

    LocalDate dt_new;
    switch (rule) {
      case "F":
        dt_new = dt.plus(days, ChronoUnit.DAYS);
        if (!cal.isBusinessDay(dt_new)) {
          return offsetBusinessDay(dt_new, cal, 1, rule);
        } else {
          return dt_new;
        }
      case "MF":
        dt_new = dt.plus(days, ChronoUnit.DAYS);
        if (!cal.isBusinessDay(dt_new)) {
          return offsetBusinessDay(dt_new, cal, 1, rule);
        } else {
          return dt_new;
        }
      case "MP":
        dt_new = dt.plus(days, ChronoUnit.DAYS);
        if (!cal.isBusinessDay(dt_new)) {
          return offsetBusinessDay(dt_new, cal, 1, rule);
        } else {
          return dt_new;
        }
      case "P":
        dt_new = dt.plus(days, ChronoUnit.DAYS);
        if (!cal.isBusinessDay(dt_new)) {
          return offsetBusinessDay(dt_new, cal, 1, rule);
        } else {
          return dt_new;
        }
      default:
        throw new RuntimeException();
    }
  }

  public static List<LocalDate> businessDaysBetween(LocalDate start, LocalDate end,
      BusinessCalendar cal) {

    List<LocalDate> dates = new LinkedList<>();
    LocalDate dt = start;
    while (dt.isBefore(end)) {
      dt = dt.plusDays(1);
      if (!cal.isBusinessDay(dt)) {
        dates.add(dt);
      }
    }
    return dates;
  }

  public static Function<LocalDate, Double> tradingTimeInterp(LocalDate start, LocalDate end,
      BusinessCalendar cal) {

    List<LocalDate> dates = businessDaysBetween(start, end, cal);
    double[] dblDates = dates.stream().mapToDouble(value -> (double) value.toEpochDay()).toArray();
    double[] idx = IntStream.range(0, dblDates.length).mapToDouble(v->(double)v).toArray();
    LinearInterpolator li = new LinearInterpolator();
    return dt -> li.interpolate(dblDates, idx).value((double) dt.toEpochDay());
  }

  public static int leapDaysBetween(LocalDate start, LocalDate end) {

    int leaps = 0;
    for (int yr = start.getYear(); yr < end.getYear(); ++yr) {
      if (Year.isLeap(yr)) {
        ++leaps;
      }
    }

    if (leaps > 0) {
      if (Year.isLeap(start.getYear())) {
        if (LocalDate.of(start.getYear(), 2, 29).isBefore(start)) {
          --leaps;
        }
      }
      if (Year.isLeap(end.getYear())) {
        if (LocalDate.of(end.getYear(), 2, 29).isAfter(end)) {
          --leaps;
        }
      }
    }
    return leaps;
  }

  public static Stream<LocalDate> stream(LocalDate startInclusive, LocalDate endExclusive) {

    Iterator<LocalDate> it = new Iterator<LocalDate>() {

      private LocalDate current = startInclusive;

      @Override
      public LocalDate next() {

        LocalDate result = current;
        current = current.plus(1, ChronoUnit.DAYS);
        return result;
      }

      @Override
      public boolean hasNext() {

        return current.isBefore(endExclusive);
      }
    };
    long count = endExclusive.toEpochDay() - startInclusive.toEpochDay() + 1;
    Spliterator<LocalDate> spliterator = Spliterators.spliterator(it, count,
        Spliterator.IMMUTABLE | Spliterator.NONNULL | Spliterator.DISTINCT | Spliterator.ORDERED
            | Spliterator.SORTED | Spliterator.SIZED | Spliterator.SUBSIZED);
    return StreamSupport.stream(spliterator, false);
  }

}



