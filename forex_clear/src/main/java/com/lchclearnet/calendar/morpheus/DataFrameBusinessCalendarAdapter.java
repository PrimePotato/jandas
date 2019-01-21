package com.lchclearnet.calendar.morpheus;

import com.lchclearnet.calendar.BusinessCalendarService;
import com.lchclearnet.fx.AppConfig;
import com.lchclearnet.request.AbstractSmartService;
import com.lchclearnet.request.SmartRequest;
import com.lchclearnet.utils.Currency;
import com.lchclearnet.utils.CurrencyPair;
import com.lchclearnet.utils.morpheus.CsvAccessor;
import com.zavtech.morpheus.frame.DataFrame;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class DataFrameBusinessCalendarAdapter extends AbstractSmartService<BusinessCalendarService> {

    private final AppConfig config;
    private final ConcurrentMap<Map<String, Object>, DataFrame<Integer, String>> data;

    public DataFrameBusinessCalendarAdapter(AppConfig config) {
        this.config = config;
        this.data = new ConcurrentHashMap<>();
    }


    public BusinessCalendarService submit(SmartRequest request) {
        BusinessCalendarService bcs = new DataFrameBusinessCalendarService(data.computeIfAbsent(request.exportParams(), this::load));
        Currency.setCalendarService(bcs);
        CurrencyPair.setCalendarService(bcs);
        return bcs;
    }

    private DataFrame<Integer, String> load(Map<String, Object> request) {
        CsvAccessor<Integer> accessor = CsvAccessor.of(config, AppConfig.CALENDAR);
        return accessor.load();
    }
}
