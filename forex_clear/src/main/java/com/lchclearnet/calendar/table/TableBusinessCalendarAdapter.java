package com.lchclearnet.calendar.table;

import com.lchclearnet.calendar.BusinessCalendarService;
import com.lchclearnet.fx.AppConfig;
import com.lchclearnet.request.AbstractSmartService;
import com.lchclearnet.request.SmartRequest;
import com.lchclearnet.table.Table;
import com.lchclearnet.table.io.csv.TableCsvReader;
import com.lchclearnet.utils.Currency;
import com.lchclearnet.utils.CurrencyPair;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class TableBusinessCalendarAdapter extends AbstractSmartService<BusinessCalendarService> {

    private final AppConfig config;
    private final ConcurrentMap<Map<String, Object>, Table> data;

    public TableBusinessCalendarAdapter(AppConfig config) {
        this.config = config;
        this.data = new ConcurrentHashMap<>();
    }


    public BusinessCalendarService submit(SmartRequest request) {
        BusinessCalendarService bcs = new TableBusinessCalendarService(data.computeIfAbsent(request.exportParams(), this::load));
        Currency.setCalendarService(bcs);
        CurrencyPair.setCalendarService(bcs);
        return bcs;
    }

    private Table load(Map<String, Object> request) {
        TableCsvReader reader = TableCsvReader.of(config, AppConfig.CALENDAR);
        return reader.read();
    }
}
