package com.lchclearnet.jandas.column.parsers;

import com.google.common.base.Strings;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

public class DateTimeParser extends AbstractParser<LocalDateTime> {

    private static final DateTimeFormatter dtTimef0 =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");     // 2014-07-09 13:03:44

    private static final DateTimeFormatter dtTimef2 =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");   // 2014-07-09 13:03:44.7 (as above, but without leading 0 in millis

    private static final DateTimeFormatter dtTimef4 =
            DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm");       // 09-Jul-2014 13:03

    private static final DateTimeFormatter dtTimef5 = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private static final DateTimeFormatter dtTimef6 = new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .append(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            .appendLiteral('.')
            .appendPattern("SSS")
            .toFormatter();                // ISO, with millis appended

    private static final DateTimeFormatter dtTimef7 =               //  7/9/14 9:04
            DateTimeFormatter.ofPattern("M/d/yy H:mm");

    private static final DateTimeFormatter dtTimef8 =
            DateTimeFormatter.ofPattern("M/d/yyyy h:mm:ss a");      //  7/9/2014 9:04:55 PM


    // A formatter that handles date time formats defined above
    public static final DateTimeFormatter DEFAULT_FORMATTER =
            new DateTimeFormatterBuilder()
                    .appendOptional(dtTimef7)
                    .appendOptional(dtTimef8)
                    .appendOptional(dtTimef2)
                    .appendOptional(dtTimef4)
                    .appendOptional(dtTimef0)
                    .appendOptional(dtTimef5)
                    .appendOptional(dtTimef6)
                    .toFormatter();

    private Locale locale = Locale.getDefault();
    private DateTimeFormatter formatter = DEFAULT_FORMATTER;


    public DateTimeParser() {
        this((Iterable<String>) null, null, null);
        ;
    }

    public DateTimeParser(String missingValueStrings, DateTimeFormatter formatter, Locale locale) {
        this(Arrays.stream(missingValueStrings.split(",")).map(s -> s.trim()).collect(Collectors.toList()), formatter, locale);
    }

    public DateTimeParser(String[] missingValueStrings, DateTimeFormatter formatter, Locale locale) {
        this(Arrays.asList(missingValueStrings), formatter, locale);
    }

    public DateTimeParser(Iterable<String> missingValueStrings, DateTimeFormatter formatter, Locale locale) {
        super(missingValueStrings);
        this.formatter = formatter != null ? formatter : this.formatter;
        this.locale = locale != null ? locale : this.locale;
    }

    @Override
    public boolean canParse(String s) {
        if (isMissing(s)) return true;

        try {
            LocalDateTime.parse(s, formatter.withLocale(locale));
            return true;
        } catch (DateTimeParseException e) {
            // it's all part of the plan
            return false;
        }
    }

    @Override
    public LocalDateTime parse(String value) {
        if (isMissing(value)) return null;

        value = Strings.padStart(value, 4, '0');
        return LocalDateTime.parse(value, formatter);
    }

  @Override
  public Class<LocalDateTime> elementClass() {

    return LocalDateTime.class;
  }
}
