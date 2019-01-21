package com.lchclearnet.table.column.datetimes;

import javax.annotation.concurrent.Immutable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.lchclearnet.table.column.datetimes.PackedLocalDateTime.asLocalDateTime;
import static com.lchclearnet.table.column.times.TimeColumn.MISSING_VALUE;

@Immutable
public class DateTimeColumnFormatter {

    private final DateTimeFormatter format;
    private String missingValueString = "";

    public DateTimeColumnFormatter() {
        this.format = null;
    }

    public DateTimeColumnFormatter(DateTimeFormatter format) {
        this.format = format;
    }

    public DateTimeColumnFormatter(DateTimeFormatter format, String missingValueString) {
        this.format = format;
        this.missingValueString = missingValueString;
    }

    public String format(long value) {
        if (value == MISSING_VALUE) {
            return missingValueString;
        }
        if (format == null) {
            return PackedLocalDateTime.toString(value);
        }
        LocalDateTime time = asLocalDateTime(value);
        if (time == null) {
            return "";
        }
        return format.format(time);
    }

    @Override
    public String toString() {
        return "DateTimeColumnFormatter{" +
                "format=" + format +
                ", missingValueString='" + missingValueString + '\'' +
                '}';
    }
}
