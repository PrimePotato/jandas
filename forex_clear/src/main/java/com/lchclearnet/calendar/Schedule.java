package com.lchclearnet.calendar;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Schedule {

    public List<LocalDate> end_dates;
    public List<LocalDate> start_dates;
    private Tenor frequency;
    private LocalDate effective_date;
    private LocalDate termination_date;
    private BusinessCalendar calendar;
    private BusinessDayConvention bdc;

    public Schedule(Tenor frequency, LocalDate effective_date,
                    LocalDate termination_date, BusinessCalendar cal, BusinessDayConvention bdc) {

        this.frequency = frequency.flip();
        this.calendar = cal;
        this.effective_date = effective_date;
        this.termination_date = termination_date;
        this.bdc = bdc;
        this.build();

    }

    private void build() {
        List<LocalDate> l = new ArrayList<>();
        start_dates = new ArrayList<>();

        LocalDate dt = this.termination_date;
        while (dt.isAfter(this.effective_date)) {
            l.add(dt);
            dt = DateUtils.resolveTenor(dt, this.frequency, this.calendar);
        }
        this.end_dates = l.stream().map(d -> this.bdc.resolveToDay(d, this.calendar))
                .collect(Collectors.toList());

        this.start_dates.addAll(this.end_dates);
        this.start_dates.add(this.effective_date);
        this.start_dates.remove(0);
    }

}
