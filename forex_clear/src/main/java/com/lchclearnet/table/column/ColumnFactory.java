package com.lchclearnet.table.column;

import com.lchclearnet.table.Column;
import com.lchclearnet.table.column.booleans.BooleanColumnType;
import com.lchclearnet.table.column.currency.CurrencyColumnType;
import com.lchclearnet.table.column.currencypair.CurrencyPairColumnType;
import com.lchclearnet.table.column.dates.DateColumnType;
import com.lchclearnet.table.column.datetimes.DateTimeColumnType;
import com.lchclearnet.table.column.enums.EnumColumnType;
import com.lchclearnet.table.column.numbers.*;
import com.lchclearnet.table.column.parsers.*;
import com.lchclearnet.table.column.strings.StringColumnType;
import com.lchclearnet.table.column.strings.TextColumnType;
import com.lchclearnet.table.column.times.TimeColumnType;
import com.lchclearnet.utils.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.HashMap;
import java.util.Map;

public class ColumnFactory {

    public static final String SKIP = "skip";

    public static final String BOOLEAN = "boolean";
    public static final String SHORT = "short";
    public static final String INT = "int";
    public static final String LONG = "long";
    public static final String FLOAT = "float";
    public static final String DOUBLE = "double";
    public static final String DATE = "date";
    public static final String TIME = "time";
    public static final String DATE_TIME = "datetime";
    public static final String STRING = "string";
    public static final String TEXT = "text";
    public static final String CURRENCY = "currency";
    public static final String CURRENCY_PAIR = "currencypair";
    public static final String SHIFT_TYPE = "shifttype";
    public static final String VOL_NODE = "volnode";
    public static final String TRADE_STATE = "tradestate";
    public static final String TRADE_TYPE = "tradetype";
    public static final String BUY_SELL = "buysell";
    public static final String CALL_PUT = "callput";

    private static Map<Class<?>, String> CLASS_2_TYPE = new HashMap<>();

    static {
        CLASS_2_TYPE.put(Boolean.class, BOOLEAN);
        CLASS_2_TYPE.put(short.class, INT);
        CLASS_2_TYPE.put(Short.class, INT);
        CLASS_2_TYPE.put(int.class, INT);
        CLASS_2_TYPE.put(Integer.class, INT);
        CLASS_2_TYPE.put(long.class, LONG);
        CLASS_2_TYPE.put(Long.class, LONG);
        CLASS_2_TYPE.put(double.class, DOUBLE);
        CLASS_2_TYPE.put(Double.class, DOUBLE);
        CLASS_2_TYPE.put(LocalDate.class, DATE);
        CLASS_2_TYPE.put(LocalTime.class, TIME);
        CLASS_2_TYPE.put(LocalDateTime.class, DATE_TIME);
        CLASS_2_TYPE.put(String.class, STRING);
        CLASS_2_TYPE.put(Currency.class, CURRENCY);
        CLASS_2_TYPE.put(CurrencyPair.class, CURRENCY_PAIR);
        CLASS_2_TYPE.put(ShiftType.class, SHIFT_TYPE);
        CLASS_2_TYPE.put(VolNode.class, VOL_NODE);
        CLASS_2_TYPE.put(TradeState.class, TRADE_STATE);
        CLASS_2_TYPE.put(TradeType.class, TRADE_TYPE);
        CLASS_2_TYPE.put(BuySell.class, BUY_SELL);
        CLASS_2_TYPE.put(CallPut.class, CALL_PUT);
    }

    public static String type(Class<?> type) {
        return CLASS_2_TYPE.get(type);
    }

    public static ColumnType columnType(Class<?> type) {
        return columnType(CLASS_2_TYPE.get(type));
    }

    public static ColumnType columnType(String type) {
        ColumnType columnType;
        switch (type.toLowerCase()) {
            case SKIP:
                columnType = ColumnType.SKIP.INSTANCE;
                break;
            case BOOLEAN:
                columnType = BooleanColumnType.INSTANCE;
                break;
            case SHORT:
                columnType = ShortColumnType.INSTANCE;
                break;
            case INT:
                columnType = IntColumnType.INSTANCE;
                break;
            case LONG:
                columnType = LongColumnType.INSTANCE;
                break;
            case FLOAT:
                columnType = FloatColumnType.INSTANCE;
                break;
            case DOUBLE:
                columnType = DoubleColumnType.INSTANCE;
                break;
            case DATE:
                columnType = DateColumnType.INSTANCE;
                break;
            case TIME:
                columnType = TimeColumnType.INSTANCE;
                break;
            case DATE_TIME:
                columnType = DateTimeColumnType.INSTANCE;
                break;
            case STRING:
                columnType = StringColumnType.INSTANCE;
                break;
            case TEXT:
                columnType = TextColumnType.INSTANCE;
                break;
            case CURRENCY:
                columnType = CurrencyColumnType.INSTANCE;
                break;
            case CURRENCY_PAIR:
                columnType = CurrencyPairColumnType.INSTANCE;
                break;
            case SHIFT_TYPE:
                columnType = EnumColumnType.INSTANCE(ShiftType.class);
                break;
            case VOL_NODE:
                columnType = EnumColumnType.INSTANCE(VolNode.class);
                break;
            case TRADE_STATE:
                columnType = EnumColumnType.INSTANCE(TradeState.class);
                break;
            case TRADE_TYPE:
                columnType = EnumColumnType.INSTANCE(TradeType.class);
                break;
            case BUY_SELL:
                columnType = EnumColumnType.INSTANCE(BuySell.class);
                break;
            case CALL_PUT:
                columnType = EnumColumnType.INSTANCE(CallPut.class);
                break;
            default:
                throw new IllegalArgumentException(String.format("Could not find column type for %s", type));

        }
        return columnType;
    }

    public static <T> AbstractParser<T> columnParser(String type, String... params) {
        if (SKIP.equalsIgnoreCase(type)) return null;
        AbstractParser<?> columnParser;
        switch (type.toLowerCase()) {
            case BOOLEAN:
                columnParser = new BooleanParser();
                break;
            case SHORT:
                columnParser = new ShortParser();
                break;
            case INT:
                columnParser = new IntParser();
                break;
            case LONG:
                columnParser = new LongParser();
                break;
            case FLOAT:
                columnParser = new FloatParser();
                break;
            case DOUBLE:
                columnParser = new DoubleParser();
                break;
            case DATE:
                DateParser dateParser = new DateParser();
                if (params.length > 0) {
                    DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
                    String[] patterns = params[0].split("$");
                    for (String pattern : patterns) {
                        builder.appendOptional(DateTimeFormatter.ofPattern(pattern.trim()));
                    }
                    dateParser.setCustomFormatter(builder.toFormatter());
                }
                columnParser = dateParser;
                break;
            case TIME:
                columnParser = new TimeParser();
                break;
            case DATE_TIME:
                columnParser = new DateTimeParser();
                break;
            case STRING:
                columnParser = new StringParser();
                break;
            case TEXT:
                columnParser = new StringParser();
                break;
            case CURRENCY:
                columnParser = new CurrencyParser();
                break;
            case CURRENCY_PAIR:
                columnParser = new CurrencyPairParser();
                break;
            case SHIFT_TYPE:
                columnParser = new ShiftTypeParser();
                break;
            case VOL_NODE:
                columnParser = new VolNodeParser();
                break;
            case TRADE_STATE:
                columnParser = new EnumParser(TradeState.class);
                break;
            case TRADE_TYPE:
                columnParser = new EnumParser(TradeType.class);
                break;
            case BUY_SELL:
                columnParser = new EnumParser(BuySell.class);
                break;
            case CALL_PUT:
                columnParser = new EnumParser(CallPut.class);
                break;
            default:
                throw new IllegalArgumentException(String.format("Could not find parsers for %s", type));
        }

        return (AbstractParser<T>) columnParser;
    }

    public static <T> Column<T> createColumn(String colName, Class<T> colType) {
        ColumnType columnType = columnType(colType);
        return (Column<T>) columnType.create(colName);
    }

    public static <T> Column<T> createColumn(String colName, String colType) {
        ColumnType columnType = columnType(colType.toLowerCase());
        return (Column<T>) columnType.create(colName);
    }

    public static <T> Column<T> createColumn(String colName, String colType, int initialSize) {
        ColumnType columnType = columnType(colType);
        return (Column<T>) columnType.create(colName, initialSize);
    }

    public static <T> Column<T> createColumn(String colName, Class<T> colType, int initialSize) {
        ColumnType columnType = columnType(colType);
        return (Column<T>) columnType.create(colName, initialSize);
    }
}
