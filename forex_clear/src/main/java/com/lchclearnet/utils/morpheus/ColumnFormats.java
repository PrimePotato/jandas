package com.lchclearnet.utils.morpheus;

import com.google.common.collect.ImmutableList;
import com.lchclearnet.mutator.morpheus.ShiftTypeParser;
import com.lchclearnet.utils.*;
import com.zavtech.morpheus.source.CsvSourceOptions;
import com.zavtech.morpheus.util.text.Formats;
import com.zavtech.morpheus.util.text.parser.Parser;

import java.math.BigDecimal;
import java.time.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.function.Supplier;

public class ColumnFormats {
    private static Supplier<String> nullValue = () -> "null";

    private static ImmutableList<String> MISSING_INDICATORS = ImmutableList.of("null", "NULL", "Null",
            "N/A", "NA", "na",
            "NaN", "nan", "Nan", "n/a",
            "*", "-");

    private static Map<String, Class> COLUMN_TYPES = new HashMap<>();
    private static Map<String, Parser> COLUMN_PARSERS = new HashMap<>();

    static {
        COLUMN_TYPES.put("boolean", boolean.class);
        COLUMN_TYPES.put("Boolean", Boolean.class);
        COLUMN_TYPES.put("int", int.class);
        COLUMN_TYPES.put("Integer", Integer.class);
        COLUMN_TYPES.put("long", long.class);
        COLUMN_TYPES.put("Long", Long.class);
        COLUMN_TYPES.put("double", double.class);
        COLUMN_TYPES.put("Double", Double.class);
        COLUMN_TYPES.put("BigDecimal", BigDecimal.class);
        COLUMN_TYPES.put("LocalDate", LocalDate.class);
        COLUMN_TYPES.put("LocalTime", LocalTime.class);
        COLUMN_TYPES.put("LocalDateTime", LocalDateTime.class);
        COLUMN_TYPES.put("ZonedDateTime", ZonedDateTime.class);
        COLUMN_TYPES.put("Period", Period.class);
        COLUMN_TYPES.put("ZoneId", ZoneId.class);
        COLUMN_TYPES.put("Month", Month.class);
        COLUMN_TYPES.put("TimeZone", TimeZone.class);
        COLUMN_TYPES.put("Date", Date.class);
        COLUMN_TYPES.put("String", String.class);
        COLUMN_TYPES.put("Object", Object.class);

        COLUMN_TYPES.put("Currency", Currency.class);
        COLUMN_TYPES.put("CurrencyPair", CurrencyPair.class);
        COLUMN_TYPES.put("ShiftType", ShiftType.class);
        COLUMN_TYPES.put("VolNode", VolNode.class);
        COLUMN_TYPES.put("TradeState", TradeState.class);
        COLUMN_TYPES.put("TradeType", TradeType.class);


        COLUMN_PARSERS.put("Currency", new CurrencyParser());
        COLUMN_PARSERS.put("CurrencyPair", new CurrencyPairParser());
        COLUMN_PARSERS.put("ShiftType", new ShiftTypeParser());
        COLUMN_PARSERS.put("VolNode", new DeltaParser());
    }


    public static <T> Class<T> getColumnType(String type) {
        return COLUMN_TYPES.get(type);
    }


    public static void setColumnFormat(CsvSourceOptions options, String column, String type, String... params) {
        Formats formats = options.getFormats();
        switch (type) {
            case "Double":
            case "double":
                if (params.length == 0) {
                    options.setColumnType(column, COLUMN_TYPES.get(type));
                } else if (params.length == 0) {
                    formats.setParser(column, Parser.ofDouble(params[0], 1));
                } else {
                    formats.setParser(column, Parser.ofDouble(params[0], Integer.parseInt(params[1])));
                }
                break;
            case "LocalDate":
                if (params.length == 0) {
                    options.setColumnType(column, COLUMN_TYPES.get(type));
                } else {
                    formats.setParser(column, Parser.ofLocalDate(params[0]));
                }
                break;
            case "LocalDateTime":
                if (params.length == 0) {
                    options.setColumnType(column, COLUMN_TYPES.get(type));
                } else {
                    formats.setParser(column, Parser.ofLocalDateTime(params[0]));
                }
                break;
            default:
                if (COLUMN_PARSERS.containsKey(type)) {
                    formats.setParser(column, COLUMN_PARSERS.get(type));
                } else {
                    options.setColumnType(column, COLUMN_TYPES.get(type));
                }


        }
    }
}
