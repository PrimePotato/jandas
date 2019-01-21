package com.lchclearnet.calendar;

import com.lchclearnet.utils.Currency;

public interface BusinessCalendarService {

    BusinessCalendar getBusinessCalendar(String calendarName);

    BusinessCalendar getBusinessCalendar(Currency currency);

    String getBusinessCalendarName(Currency currency);
}
