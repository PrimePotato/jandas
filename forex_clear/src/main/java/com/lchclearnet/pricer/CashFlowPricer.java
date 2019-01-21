package com.lchclearnet.pricer;

import com.lchclearnet.calendar.BusinessCalendarService;
import com.lchclearnet.market.FxMarket;

import java.time.LocalDate;

public interface CashFlowPricer<INST, RES> extends Pricer<INST, RES> {
    RES price(LocalDate valueDate, INST cashFlowInstruments, FxMarket mktServiceProvider, BusinessCalendarService calendarService);
}
