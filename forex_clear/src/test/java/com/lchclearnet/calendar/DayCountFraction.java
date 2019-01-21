package com.lchclearnet.calendar;


import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public enum DayCountFraction implements DayCalc {
    DCF_30_360("30/360") {
        public double calc(LocalDate start, LocalDate end) {

            return (360. * (end.getYear() - start.getYear()) + 30. * (end.getMonthValue()
                    - start.getMonthValue()) + (end.getDayOfMonth() - start.getDayOfMonth())) / 360.;
        }
    },

    DCF_ACT_ACT("ACT/ACT") {
        public double calc(LocalDate start, LocalDate end) {

            int leaps = DateUtils.leapDaysBetween(start, end);
            long d = ChronoUnit.DAYS.between(start, end) - leaps;
            d = d - leaps;
            return d / 365. + leaps / 360.;
        }
    },

    DCF_ACT_365F("ACT/365F") {
        public double calc(LocalDate start, LocalDate end) {

            long d = ChronoUnit.DAYS.between(start, end);
            return d / 365.;
        }
    },

    DCF_ACT_360("ACT/360") {
        public double calc(LocalDate start, LocalDate end) {

            long d = ChronoUnit.DAYS.between(start, end);
            return d / 360.;
        }
    };

    private String label;

    DayCountFraction(String label) {

        this.label = label;
    }

    @Override
    public String toString() {

        return label;
    }
}
