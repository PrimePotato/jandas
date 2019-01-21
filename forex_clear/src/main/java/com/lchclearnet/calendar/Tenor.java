package com.lchclearnet.calendar;

import java.time.temporal.ChronoUnit;

public class Tenor {

    private int value;
    private ChronoUnit unit;
    private boolean businessDayOffset;

    public Tenor(int v, ChronoUnit u) {

        this(v, u, false);
    }

    public Tenor(int v, ChronoUnit u, boolean offset) {

        value = v;
        unit = u;
        businessDayOffset = offset;
    }

    public static Tenor parse(String t) {

        int value;
        ChronoUnit unit;
        boolean business_day_offset;

        String str_unit = t.substring(t.length() - 1);

        business_day_offset = false;
        switch (str_unit) {
            case "D":
                value = Integer.parseInt(t.substring(0, t.length() - 1));
                unit = ChronoUnit.DAYS;
                break;
            case "M":
                value = Integer.parseInt(t.substring(0, t.length() - 1));
                unit = ChronoUnit.MONTHS;
                break;
            case "W":
                value = Integer.parseInt(t.substring(0, t.length() - 1));
                unit = ChronoUnit.WEEKS;
                break;
            case "Y":
                value = Integer.parseInt(t.substring(0, t.length() - 1));
                unit = ChronoUnit.YEARS;
                break;
            case "B":
                value = Integer.parseInt(t.substring(0, t.length() - 1));
                unit = ChronoUnit.DAYS;
                business_day_offset = true;
                break;
            case "N":
                unit = ChronoUnit.DAYS;
                business_day_offset = true;
                switch (t.substring(1)) {
                    case "O":
                        value = 1;
                        break;
                    case "T":
                        value = 2;
                        break;
                    case "S":
                        value = 3; //TODO: Spot days should be implied from currency pair
                        break;
                    default:
                        throw new RuntimeException(
                                "%s unit as as an unrecognised spot prefix %s".format(Tenor.class.getName(),
                                        str_unit));
                }
                break;
            default:
                throw new RuntimeException("%s unit not valid %s".format(Tenor.class.getName(), str_unit));
        }

        return new Tenor(value, unit, business_day_offset);

    }

    public Tenor flip() {

        return new Tenor(-value, unit, businessDayOffset);
    }

    public int value() {

        return value;
    }

    public ChronoUnit unit() {

        return unit;
    }

    public boolean businessDayOffset() {

        return businessDayOffset;
    }

    @Override
    public String toString() {

        return String.format("Tenor{value=%s, unit=%s, businessDayOffset=%s", value, unit,
                businessDayOffset);
    }

}
