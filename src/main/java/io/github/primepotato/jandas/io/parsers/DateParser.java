package io.github.primepotato.jandas.io.parsers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class DateParser extends AbstractParser<LocalDate> {

  private static final List<String> acceptedPatterns =
      Arrays.asList("dd/MM/yyyy", "yyyyMMdd");

  private static DateTimeFormatter getDefaultFormatter() {

    DateTimeFormatterBuilder dfb = new DateTimeFormatterBuilder();
    acceptedPatterns.forEach(p -> dfb.appendOptional(DateTimeFormatter.ofPattern(p)));
    return dfb.toFormatter();
  }

  public static final DateTimeFormatter DEFAULT_FORMATTER = getDefaultFormatter();

  private Locale locale = Locale.getDefault();
  private DateTimeFormatter formatter = DEFAULT_FORMATTER;

  public DateParser() {

    this((Iterable<String>) null, null, null);
    ;
  }

  public DateParser(String missingValueStrings, DateTimeFormatter formatter, Locale locale) {

    this(Arrays.stream(missingValueStrings.split(","))
        .map(s -> s.trim())
        .collect(Collectors.toList()), formatter, locale);
  }

  public DateParser(String[] missingValueStrings, DateTimeFormatter formatter, Locale locale) {

    this(Arrays.asList(missingValueStrings), formatter, locale);
  }

  public DateParser(Iterable<String> missingValueStrings, DateTimeFormatter formatter,
      Locale locale) {

    super(missingValueStrings);
    this.formatter = formatter != null ? formatter : this.formatter;
    this.locale = locale != null ? locale : this.locale;
  }

  @Override
  public boolean canParse(String s) {

    if (isMissing(s)) {
      return true;
    }

    try {
      LocalDate.parse(s, formatter.withLocale(locale));
      return true;
    } catch (DateTimeParseException e) {
      // it's all part of the plan
      return false;
    }
  }

  public void setCustomFormatter(DateTimeFormatter f) {

    formatter = f;
  }

  public void setLocale(Locale locale) {

    this.locale = locale;
  }

  @Override
  public LocalDate parse(String s) {

    if (isMissing(s)) {
      return null;
    }
    return LocalDate.parse(s, formatter);
  }

  @Override
  public Class<LocalDate> elementClass() {

    return LocalDate.class;
  }

}
