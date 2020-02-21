package io.github.primepotato.jandas.io.parsers.impl;

import com.google.common.base.Strings;
import io.github.primepotato.jandas.io.parsers.AbstractParser;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

public class TimeParser extends AbstractParser<LocalTime> {

    private static final DateTimeFormatter timef1 = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
    private static final DateTimeFormatter timef2 = DateTimeFormatter.ofPattern("hh:mm:ss a");
    private static final DateTimeFormatter timef3 = DateTimeFormatter.ofPattern("h:mm:ss a");
    private static final DateTimeFormatter timef4 = DateTimeFormatter.ISO_LOCAL_TIME;
    private static final DateTimeFormatter timef5 = DateTimeFormatter.ofPattern("hh:mm a");
    private static final DateTimeFormatter timef6 = DateTimeFormatter.ofPattern("h:mm a");

    // only for parsing:
    private static final DateTimeFormatter timef7 = DateTimeFormatter.ofPattern("HHmm");

    // A formatter that handles time formats defined above used for type detection.
    // It is more conservative than the converter
    private static final DateTimeFormatter TIME_DETECTION_FORMATTER =
            new DateTimeFormatterBuilder()
                    .appendOptional(timef5)
                    .appendOptional(timef2)
                    .appendOptional(timef3)
                    .appendOptional(timef1)
                    .appendOptional(timef4)
                    .appendOptional(timef6)
                    .toFormatter();

    // A formatter that handles time formats defined above
    /**
     * A formatter for parsing. Useful when the user has specified that a numeric-like column is really supposed to be a time
     * See timef7 definition
     */
    private static final DateTimeFormatter TIME_CONVERSION_FORMATTER =
            new DateTimeFormatterBuilder()
                    .appendOptional(timef5)
                    .appendOptional(timef2)
                    .appendOptional(timef3)
                    .appendOptional(timef1)
                    .appendOptional(timef4)
                    .appendOptional(timef6)
                    .appendOptional(timef7)
                    .toFormatter();

    private static final DateTimeFormatter DEFAULT_FORMATTER = TIME_DETECTION_FORMATTER;

    private Locale locale = Locale.getDefault();

    private DateTimeFormatter formatter = DEFAULT_FORMATTER;
    private DateTimeFormatter parserFormatter = TIME_CONVERSION_FORMATTER;

    public TimeParser() {
        this((Iterable<String>) null, null, null);
    }

    public TimeParser(String missingValueStrings, DateTimeFormatter formatter, Locale locale) {
        this(Arrays.stream(missingValueStrings.split(",")).map(s -> s.trim()).collect(Collectors.toList()), formatter, locale);
    }

    public TimeParser(String[] missingValueStrings, DateTimeFormatter formatter, Locale locale) {
        this(Arrays.asList(missingValueStrings), formatter, locale);
    }

    public TimeParser(Iterable<String> missingValueStrings, DateTimeFormatter formatter, Locale locale) {
        super(missingValueStrings);
        this.formatter = formatter != null ? formatter : this.formatter;
        this.parserFormatter = formatter != null ? formatter : this.parserFormatter;
        this.locale = locale != null ? locale : this.locale;
    }


    @Override
    public boolean canParse(String s) {
        if (isMissing(s)) {
            return true;
        }
        try {
            LocalTime.parse(s, formatter.withLocale(locale));
            return true;
        } catch (DateTimeParseException e) {
            // it's all part of the plan
            return false;
        }
    }

    @Override
    public LocalTime parse(String value) {
        if (isMissing(value)) {
            return null;
        }
        value = Strings.padStart(value, 4, '0');
        return LocalTime.parse(value, parserFormatter);
    }

  @Override
  public Class<LocalTime> elementClass() {

    return LocalTime.class;
  }

}
