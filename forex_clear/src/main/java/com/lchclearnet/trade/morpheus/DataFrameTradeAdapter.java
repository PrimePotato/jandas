package com.lchclearnet.trade.morpheus;

import com.lchclearnet.fx.AppConfig;
import com.lchclearnet.request.AbstractSmartService;
import com.lchclearnet.request.SmartRequest;
import com.lchclearnet.trade.TradeService;
import com.lchclearnet.utils.morpheus.CsvAccessor;
import com.zavtech.morpheus.frame.DataFrame;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class DataFrameTradeAdapter extends AbstractSmartService<TradeService> {

    private final AppConfig config;
    private final ConcurrentMap<Map<String, Object>, DataFrame<Long, String>> data;

    public DataFrameTradeAdapter(AppConfig config) {
        this.config = config;
        this.data = new ConcurrentHashMap<>();
    }

    public DataFrameTradeService submit(SmartRequest request) {
        return new DataFrameTradeService(data.computeIfAbsent(request.exportParams(), this::load));
    }

    private DataFrame<Long, String> load(Map<String, Object> request) {
        CsvAccessor<Long> accessor = CsvAccessor.of(config, AppConfig.TRADE);
        return accessor.load();
    }

}
