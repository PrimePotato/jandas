package com.lchclearnet.calendar;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class SingleBusinessCalendar implements BusinessCalendar {

    private Set<DayOfWeek> weekend;
    private Set<LocalDate> holidays;
    private String name;

    public SingleBusinessCalendar(String n, Set<LocalDate> dts, Set<DayOfWeek> wkd) {

        name = n;
        holidays = dts;
        weekend = wkd;
    }

    public boolean isBusinessDay(LocalDate dt) {

        return !weekend.contains(dt.getDayOfWeek()) && !holidays.contains(dt);
    }

    @Override
    public String toString() {
        return name;
    }

}