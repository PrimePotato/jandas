package com.lchclearnet.table.column.dates;

import javax.annotation.concurrent.Immutable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.lchclearnet.table.column.dates.DateColumn.MISSING_VALUE;
import static com.lchclearnet.table.column.dates.PackedLocalDate.asLocalDate;
import static com.lchclearnet.table.column.dates.PackedLocalDate.toDateString;

@Immutable
public class DateColumnFormatter {

    private final DateTimeFormatter format;
    private String missingString = "";

    public DateColumnFormatter() {
        this.format = null;
    }

    public DateColumnFormatter(DateTimeFormatter format) {
        this.format = format;
    }

    public DateColumnFormatter(DateTimeFormatter format, String missingString) {
        this.format = format;
        this.missingString = missingString;
    }

    public String format(int value) {
        if (value == MISSING_VALUE) {
            return missingString;
        }
        if (format == null) {
            return toDateString(value);
        }
        LocalDate date = asLocalDate(value);
        if (date == null) {
            return "";
        }
        return format.format(date);
    }

    @Override
    public String toString() {
        return "DateColumnFormatter{" +
                "format=" + format +
                ", missingString='" + missingString + '\'' +
                '}';
    }
}
