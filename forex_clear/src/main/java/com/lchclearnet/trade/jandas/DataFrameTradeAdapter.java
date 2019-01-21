package com.lchclearnet.trade.jandas;

import com.lchclearnet.fx.AppConfig;
import com.lchclearnet.jandas.dataframe.DataFrame;
import com.lchclearnet.jandas.io.DataFrameCsvReader;
import com.lchclearnet.request.AbstractSmartService;
import com.lchclearnet.request.SmartRequest;
import com.lchclearnet.trade.TradeService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class DataFrameTradeAdapter extends AbstractSmartService<TradeService> {

  private final AppConfig config;
  private final ConcurrentMap<Map<String, Object>, DataFrame> data;

  public DataFrameTradeAdapter(AppConfig config) {

    this.config = config;
    this.data = new ConcurrentHashMap<>();
  }

  public DataFrameTradeService submit(SmartRequest request) {

    return new DataFrameTradeService(data.computeIfAbsent(request.exportParams(), this::load));
  }

  private DataFrame load(Map<String, Object> request) {

    DataFrameCsvReader reader = DataFrameCsvReader.of(config, AppConfig.TRADE);
    return reader.read();
  }

}
