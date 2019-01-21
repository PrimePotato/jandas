package com.lchclearnet.calendar;

import java.time.LocalDate;

public interface BusinessCalendar {

    boolean isBusinessDay(LocalDate dt);

}