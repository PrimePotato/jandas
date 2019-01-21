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

    public static DayCountFraction getEnum(String value) {
        if (value == null) throw new NullPointerException("Cannot Parse a null Day Count Fraction.");
        value = value.trim();
        if ("30/360".equals(value)) return DCF_30_360;
        if ("ACT/ACT".equalsIgnoreCase(value)) return DCF_ACT_ACT;
        if ("ACT/365F".equalsIgnoreCase(value)) return DCF_ACT_365F;
        if ("ACT/360".equalsIgnoreCase(value)) return DCF_ACT_360;
        throw new IllegalArgumentException(String.format("'%s' is not a valid Day Count Fraction.", value));
    }

    @Override
    public String toString() {

        return label;
    }
}
