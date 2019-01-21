package com.lchclearnet.calendar;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class BusinessCalendars {

    private BusinessCalendars() {
    }

    public static BusinessCalendar union(BusinessCalendar... calendars) {
        final Set<BusinessCalendar> cals = new HashSet<>();
        for (BusinessCalendar calendar : calendars) {
            cals.add(calendar);
        }
        return new BusinessCalendar() {
            @Override
            public boolean isBusinessDay(LocalDate dt) {
                for (BusinessCalendar calendar : cals) {
                    if (!calendar.isBusinessDay(dt)) {
                        return false;
                    }
                }
                return true;
            }

            @Override
            public String toString() {
                return "{%s}" + cals.stream().map(cal -> cal.toString()).collect(Collectors.joining(","));
            }
        };
    }
}