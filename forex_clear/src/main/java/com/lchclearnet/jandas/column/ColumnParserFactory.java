package com.lchclearnet.jandas.column;

import com.lchclearnet.jandas.io.parsers.AbstractParser;
import com.lchclearnet.jandas.io.parsers.BooleanParser;
import com.lchclearnet.jandas.io.parsers.CurrencyPairParser;
import com.lchclearnet.jandas.io.parsers.CurrencyParser;
import com.lchclearnet.jandas.io.parsers.DateParser;
import com.lchclearnet.jandas.io.parsers.DateTimeParser;
import com.lchclearnet.jandas.io.parsers.DoubleParser;
import com.lchclearnet.jandas.io.parsers.EnumParser;
import com.lchclearnet.jandas.io.parsers.IntParser;
import com.lchclearnet.jandas.io.parsers.ShiftTypeParser;
import com.lchclearnet.jandas.io.parsers.StringParser;
import com.lchclearnet.jandas.io.parsers.TimeParser;
import com.lchclearnet.jandas.io.parsers.VolNodeParser;
import com.lchclearnet.utils.BuySell;
import com.lchclearnet.utils.CallPut;
import com.lchclearnet.utils.Currency;
import com.lchclearnet.utils.CurrencyPair;
import com.lchclearnet.utils.Pair;
import com.lchclearnet.utils.ShiftType;
import com.lchclearnet.utils.TradeState;
import com.lchclearnet.utils.TradeType;
import com.lchclearnet.utils.VolNode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.HashMap;
import java.util.Map;

public class ColumnParserFactory {

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

  private static void setDateParams(DateParser dp, String... params) {

    if (params.length > 0) {
      DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
      String[] patterns = params[0].split("$");
      for (String pattern : patterns) {
        builder.appendOptional(DateTimeFormatter.ofPattern(pattern.trim()));
      }
      dp.setCustomFormatter(builder.toFormatter());
    }
  }

  public static Pair<Column, AbstractParser> createColumn(String colName, String colType,
      Boolean indexed, String... params) {

    AbstractParser columnParser;
    Column col;

    switch (colType.toLowerCase()) {
      case BOOLEAN:
        columnParser = new BooleanParser();
        col = new StringColumn(colName, indexed, new String[0]);
        break;
      case SHORT:
        columnParser = new IntParser();
        col = new IntegerColumn(colName, indexed, new int[0]);
        break;
      case INT:
        columnParser = new IntParser();
        col = new IntegerColumn(colName, indexed, new int[0]);
        break;
      case LONG:
        columnParser = new IntParser();
        col = new IntegerColumn(colName, indexed, new int[0]);
        break;
      case FLOAT:
        columnParser = new DoubleParser();
        col = new DoubleColumn(colName, indexed, new double[0]);
        break;
      case DOUBLE:
        columnParser = new DoubleParser();
        col = new DoubleColumn(colName, indexed, new double[0]);
        break;
      case DATE:
        DateParser dateParser = new DateParser();
        col = new DateColumn(colName, indexed, new LocalDate[0]);
        setDateParams(dateParser, params);
        columnParser = dateParser;
        break;
      case TIME:
        columnParser = new TimeParser();
        col = new TimeColumn(colName, indexed, new LocalTime[0]);
        break;
      case DATE_TIME:
        columnParser = new DateTimeParser();
        col = new StringColumn(colName, indexed, new String[0]);
        break;
      case STRING:
        columnParser = new StringParser();
        col = new StringColumn(colName, indexed, new String[0]);
        break;
      case TEXT:
        columnParser = new StringParser();
        col = new StringColumn(colName, indexed, new String[0]);
        break;
      case CURRENCY:
        columnParser = new CurrencyParser();
        col = new EnumColumn<>(colName, indexed, new Currency[0], Currency.class);
        break;
      case CURRENCY_PAIR:
        columnParser = new CurrencyPairParser();
        col = new CurrencyPairColumn(colName, indexed, new CurrencyPair[0]);
        break;
      case SHIFT_TYPE:
        columnParser = new ShiftTypeParser();
        col = new EnumColumn<>(colName, indexed, new ShiftType[0], ShiftType.class);
        break;
      case VOL_NODE:
        columnParser = new VolNodeParser();
        col = new EnumColumn<>(colName, indexed, new VolNode[0], VolNode.class);
        break;
      case TRADE_STATE:
        columnParser = new EnumParser<>(TradeState.class);
        col = new EnumColumn<>(colName, indexed, new TradeState[0], TradeState.class);
        break;
      case TRADE_TYPE:
        columnParser = new EnumParser<>(TradeType.class);
        col = new EnumColumn<>(colName, indexed, new TradeType[0], TradeType.class);
        break;
      case BUY_SELL:
        columnParser = new EnumParser<>(BuySell.class);
        col = new EnumColumn<>(colName, indexed, new BuySell[0], BuySell.class);
        break;
      case CALL_PUT:
        columnParser = new EnumParser<>(CallPut.class);
        col = new EnumColumn<>(colName, indexed, new CallPut[0], CallPut.class);
        break;
      default:
        throw new IllegalArgumentException(String.format("Could not find parsers for %s", colType));
    }
    return new Pair<>(col, columnParser);
  }

}
