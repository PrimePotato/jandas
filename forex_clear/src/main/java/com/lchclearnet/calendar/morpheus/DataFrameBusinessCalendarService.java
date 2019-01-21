package com.lchclearnet.calendar.morpheus;

import com.google.common.collect.ImmutableSet;
import com.lchclearnet.calendar.BusinessCalendar;
import com.lchclearnet.calendar.BusinessCalendarService;
import com.lchclearnet.calendar.SingleBusinessCalendar;
import com.lchclearnet.utils.Currency;
import com.lchclearnet.utils.FinalValue;
import com.zavtech.morpheus.frame.DataFrame;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class DataFrameBusinessCalendarService implements BusinessCalendarService {

    private final DataFrame<Integer, String> data;

    private final Map<String, BusinessCalendar> calendars;
    private final BusinessCalendar[] currencyCalendars;
    private final String[] currencyCalendarNames;

    public DataFrameBusinessCalendarService(DataFrame data) {

        this.data = data;
        this.calendars = new ConcurrentHashMap<>();
        this.currencyCalendars = new BusinessCalendar[Currency.values().length];
        this.currencyCalendarNames = new String[Currency.values().length];

    }

    @Override
    public BusinessCalendar getBusinessCalendar(String calendar) {
        calendar = calendar.replaceAll("\\s+", "");
        return calendars.computeIfAbsent(calendar, cal -> {
            final Set<DayOfWeek> weekend = ImmutableSet.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);
            final Set<LocalDate> holidays = new TreeSet<>();

            if (cal.contains("*")) {
                final List<String> calendarNames = Arrays.stream(cal.split("\\*")).collect(Collectors.toList());
                final Map<String, Set<LocalDate>> cHolidays = new HashMap<>();
                data.rows().forEach(row -> {
                    String hc = row.getValue("HolidayCalendar");
                    if (calendarNames.contains(hc)) {
                        Set<LocalDate> dts = cHolidays.computeIfAbsent(hc, h -> new HashSet<>());
                        dts.add(row.getValue("HolidayDates"));
                    }
                });
                Iterator<Set<LocalDate>> it = cHolidays.values().iterator();
                holidays.addAll(it.next());
                while (it.hasNext()) {
                    holidays.retainAll(it.next());
                }
            } else {
                final List<String> calendarNames = Arrays.stream(cal.split("\\+")).collect(Collectors.toList());
                data.rows().forEach(row -> {
                    if (calendarNames.contains(row.getValue("HolidayCalendar"))) {
                        holidays.add(row.getValue("HolidayDates"));
                    }
                });
            }

            return new SingleBusinessCalendar(cal, holidays, weekend);
        });
    }

    @Override
    public BusinessCalendar getBusinessCalendar(final Currency currency) {

        BusinessCalendar calendar = currencyCalendars[currency.ordinal()];
        if (calendar == null) {
            final Set<LocalDate> holidays = new HashSet<>();
            final Set<DayOfWeek> weekend = ImmutableSet.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);
            String ccy = currency.getCurrencyCode();
            data.rows().forEach(row -> {
                if (ccy.equals(row.getValue("Currency"))) {
                    holidays.add(row.getValue("HolidayDates"));
                }
            });
            calendar = new SingleBusinessCalendar(ccy, holidays, weekend);
            currencyCalendars[currency.ordinal()] = calendar;
        }

        return calendar;
    }

    @Override
    public String getBusinessCalendarName(final Currency currency) {
        String calendar = currencyCalendarNames[currency.ordinal()];
        if (calendar == null) {
            String ccy = currency.getCurrencyCode();
            calendar = data.rows().first(row -> ccy.equals(row.getValue("Currency"))).get().getValue("HolidayCalendar");
        }
        currencyCalendarNames[currency.ordinal()] = calendar;

        return calendar;

    }
}
