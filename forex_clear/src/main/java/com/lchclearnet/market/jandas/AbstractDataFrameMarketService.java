package com.lchclearnet.market.jandas;

import com.lchclearnet.jandas.column.Column;
import com.lchclearnet.jandas.dataframe.DataFrame;
import com.lchclearnet.jandas.dataframe.Record;
import com.lchclearnet.market.MarketService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class AbstractDataFrameMarketService implements MarketService {

  protected final DataFrame data;
  private final String marketDateFieldName;

  public AbstractDataFrameMarketService(String marketDateFieldName, DataFrame data) {

    this.marketDateFieldName = marketDateFieldName;
    this.data = data;
  }

  @Override
  public LocalDate getMarketDate() {

    return data.column(marketDateFieldName).firstValue();
  }

}
