package com.lchclearnet.trade;

import com.lchclearnet.calendar.*;
import com.zavtech.morpheus.frame.DataFrame;

import java.time.LocalDate;
import java.util.ArrayList;

public class IRSwap {

    double notional;
    Tenor frequency;
    double fixed_coupon;
    BusinessDayConvention bdc;
    DayCountFraction fixed_day_count;
    DayCountFraction float_day_count;
    LocalDate effective_date;
    LocalDate termination_date;
    Schedule schedule;
    BusinessCalendar cal;
    ArrayList<Double> fixed_time_periods;
    ArrayList<Double> float_time_periods;
    DataFrame flow_frame;

    public IRSwap(double notional, Tenor frequency, double fixed_coupon, BusinessDayConvention bdc,
                  DayCountFraction fixed_day_count, DayCountFraction float_day_count, LocalDate effective_date,
                  LocalDate termination_date, BusinessCalendar cal) {

        this.notional = notional;
        this.frequency = frequency;
        this.fixed_coupon = fixed_coupon;
        this.bdc = bdc;
        this.fixed_day_count = fixed_day_count;
        this.float_day_count = float_day_count;
        this.effective_date = effective_date;
        this.termination_date = termination_date;
        this.cal = cal;
        this.schedule = new Schedule(frequency, effective_date, termination_date, cal, bdc);

        this.fixed_time_periods = new ArrayList<>();
        this.float_time_periods = new ArrayList<>();

        for (int i = 0; i < this.schedule.start_dates.size(); i++) {
            this.fixed_time_periods.add(
                    fixed_day_count.calc(this.schedule.start_dates.get(i), this.schedule.end_dates.get(i)));
            this.float_time_periods.add(
                    float_day_count.calc(this.schedule.start_dates.get(i), this.schedule.end_dates.get(i)));
        }
    }


    public void toCashFlows() {

    }

}
