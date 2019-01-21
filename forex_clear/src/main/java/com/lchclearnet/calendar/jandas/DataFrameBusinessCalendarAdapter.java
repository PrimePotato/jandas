package com.lchclearnet.calendar.jandas;

import com.lchclearnet.calendar.BusinessCalendarService;
import com.lchclearnet.fx.AppConfig;
import com.lchclearnet.jandas.dataframe.DataFrame;
import com.lchclearnet.jandas.io.DataFrameCsvReader;
import com.lchclearnet.request.AbstractSmartService;
import com.lchclearnet.request.SmartRequest;

import com.lchclearnet.utils.Currency;
import com.lchclearnet.utils.CurrencyPair;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class DataFrameBusinessCalendarAdapter extends
    AbstractSmartService<BusinessCalendarService> {

  private final AppConfig config;
  private final ConcurrentMap<Map<String, Object>, DataFrame> data;

  public DataFrameBusinessCalendarAdapter(AppConfig config) {

    this.config = config;
    this.data = new ConcurrentHashMap<>();
  }

  public BusinessCalendarService submit(SmartRequest request) {

    BusinessCalendarService bcs = new DataFrameBusinessCalendarService(
        data.computeIfAbsent(request.exportParams(), this::load));
    Currency.setCalendarService(bcs);
    CurrencyPair.setCalendarService(bcs);
    return bcs;
  }

  private DataFrame load(Map<String, Object> request) {

    DataFrameCsvReader reader = DataFrameCsvReader.of(config, AppConfig.CALENDAR);
    return reader.read();
  }
}
