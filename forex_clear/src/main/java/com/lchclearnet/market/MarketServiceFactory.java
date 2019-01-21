package com.lchclearnet.market;

import com.zavtech.morpheus.frame.DataFrame;

import java.util.Map;

public interface MarketServiceFactory {

    <T extends MarketService> T getMarketSource(SourceType type,
                                                Map<MarketDataType<SourceType>, MarketService> serviceContext,
                                                Map<SourceType, Object> dataContext);

    <T extends MarketService> T getMarketData(DataType type,
                                              Map<MarketDataType<SourceType>, MarketService> serviceContext,
                                              Map<SourceType, Object> dataContext);
}
