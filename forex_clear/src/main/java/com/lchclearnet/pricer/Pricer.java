package com.lchclearnet.pricer;

import com.lchclearnet.calendar.BusinessCalendarService;
import com.lchclearnet.market.FxMarket;

import java.time.LocalDate;

public interface Pricer<INSTRUMENTS, RESULTS> {

    RESULTS price(LocalDate valueDate, INSTRUMENTS instruments, FxMarket mktServiceProvider, BusinessCalendarService calendarService);
}
