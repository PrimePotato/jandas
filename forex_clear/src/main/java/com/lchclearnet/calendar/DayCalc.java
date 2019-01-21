package com.lchclearnet.calendar;

import java.time.LocalDate;

public interface DayCalc {
    double calc(LocalDate startDate, LocalDate endDate);

}
