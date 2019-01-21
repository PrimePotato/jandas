package com.lchclearnet.trade.table;

import com.lchclearnet.fx.AppConfig;
import com.lchclearnet.request.AbstractSmartService;
import com.lchclearnet.request.SmartRequest;
import com.lchclearnet.table.Table;
import com.lchclearnet.table.io.csv.TableCsvReader;
import com.lchclearnet.trade.TradeService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class TableTradeAdapter extends AbstractSmartService<TradeService> {

    private final AppConfig config;
    private final ConcurrentMap<Map<String, Object>, Table> data;

    public TableTradeAdapter(AppConfig config) {
        this.config = config;
        this.data = new ConcurrentHashMap<>();
    }

    public TradeTableService submit(SmartRequest request) {
        return new TradeTableService(data.computeIfAbsent(request.exportParams(), this::load));
    }

    private Table load(Map<String, Object> request) {
        TableCsvReader reader = TableCsvReader.of(config, AppConfig.TRADE);
        return reader.read();
    }

}
