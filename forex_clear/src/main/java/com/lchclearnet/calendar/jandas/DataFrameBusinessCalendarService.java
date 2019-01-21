package com.lchclearnet.calendar.jandas;

import com.google.common.collect.ImmutableSet;
import com.lchclearnet.calendar.BusinessCalendar;
import com.lchclearnet.calendar.BusinessCalendarService;
import com.lchclearnet.calendar.SingleBusinessCalendar;
import com.lchclearnet.jandas.dataframe.DataFrame;
import com.lchclearnet.jandas.dataframe.Record;

import com.lchclearnet.utils.Currency;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class DataFrameBusinessCalendarService implements BusinessCalendarService {

    private final DataFrame data;

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
            final Set<LocalDate> holidays = new HashSet<>();

            DataFrame bc = data.select("HolidayCalendar", "HolidayDates");

            if (cal.contains("*")) {
                final List<String> calendarNames = Arrays.stream(cal.split("\\*")).collect(Collectors.toList());
                final Map<String, Set<LocalDate>> cHolidays = new HashMap<>();

                for (int i = 0; i < bc.rowCount(); ++i) {
                    String hc = bc.getString(i, 0);//"HolidayCalendar" index is 0
                    if (calendarNames.contains(hc)) {
                        Set<LocalDate> dts = cHolidays.computeIfAbsent(hc, h -> new HashSet<>());
                        dts.add(bc.getObject(i, 1));//"HolidayDates" index is 1
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
                    if (calendarNames.contains(bc.getObject(i, 0))) { //"HolidayCalendar" index is 0
                        holidays.add(bc.getObject(i, 1)); //"HolidayDates" index is 1
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
            for(Record rec : data){
                if (ccy.equals(rec.getString("Currency"))) { //"Currency" index is 0
                    holidays.add(rec.getDate("HolidayDates")); //"HolidayDates" index is 1
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
            for(Record rec: data){
                if(ccy.equals(rec.getString("Currency"))){
                    calendar = rec.getString("HolidayDates");
                    break;
                }
            }
        }
        currencyCalendarNames[currency.ordinal()] = calendar;

        return calendar;

    }
}
