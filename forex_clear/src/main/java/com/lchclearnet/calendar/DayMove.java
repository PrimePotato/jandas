package com.lchclearnet.calendar;

import java.time.LocalDate;

public interface DayMove {
    LocalDate resolveToDay(LocalDate dt, BusinessCalendar cal);

}
