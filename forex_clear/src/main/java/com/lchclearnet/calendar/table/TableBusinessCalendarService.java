package com.lchclearnet.calendar.table;

import com.google.common.collect.ImmutableSet;
import com.lchclearnet.calendar.BusinessCalendar;
import com.lchclearnet.calendar.BusinessCalendarService;
import com.lchclearnet.calendar.SingleBusinessCalendar;
import com.lchclearnet.table.Row;
import com.lchclearnet.table.Table;
import com.lchclearnet.utils.Currency;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class TableBusinessCalendarService implements BusinessCalendarService {

    private final Table data;

    private final Map<String, BusinessCalendar> calendars;
    private final BusinessCalendar[] currencyCalendars;
    private final String[] currencyCalendarNames;

    public TableBusinessCalendarService(Table data) {

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
            final Set<LocalDate> holidays = new HashSet<>();

            Table bc = data.select("HolidayCalendar", "HolidayDates");

            if (cal.contains("*")) {
                final List<String> calendarNames = Arrays.stream(cal.split("\\*")).collect(Collectors.toList());
                final Map<String, Set<LocalDate>> cHolidays = new HashMap<>();

                for (int i = 0; i < bc.rowCount(); ++i) {
                    String hc = bc.get(i, 0);//"HolidayCalendar" index is 0
                    if (calendarNames.contains(hc)) {
                        Set<LocalDate> dts = cHolidays.computeIfAbsent(hc, h -> new HashSet<>());
                        dts.add(bc.get(i, 1));//"HolidayDates" index is 1
                    }
                }

                Iterator<Set<LocalDate>> it = cHolidays.values().iterator();
                holidays.addAll(it.next());
                while (it.hasNext()) {
                    holidays.retainAll(it.next());
                }
            } else {
                final List<String> calendarNames = Arrays.stream(cal.split("\\+")).collect(Collectors.toList());
                for (int i = 0; i < bc.rowCount(); ++i) {
                    if (calendarNames.contains(bc.get(i, 0))) { //"HolidayCalendar" index is 0
                        holidays.add(bc.get(i, 1)); //"HolidayDates" index is 1
                    }
                }
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
            String ccy = currency.name();
            for(Row row : data){
                if (ccy.equals(row.getString("Currency"))) { //"Currency" index is 0
                    holidays.add(row.getDate("HolidayDates")); //"HolidayDates" index is 1
                }
            }
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
            for(Row row: data){
                if(ccy.equals(row.getString("Currency"))){
                    calendar = row.getString("HolidayDates");
                    break;
                }
            }
        }
        currencyCalendarNames[currency.ordinal()] = calendar;

        return calendar;

    }
}
