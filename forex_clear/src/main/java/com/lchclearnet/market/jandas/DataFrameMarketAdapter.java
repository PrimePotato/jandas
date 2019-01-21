package com.lchclearnet.market.jandas;

import com.lchclearnet.fx.AppConfig;
import com.lchclearnet.jandas.dataframe.DataFrame;
import com.lchclearnet.jandas.io.DataFrameCsvReader;
import com.lchclearnet.market.DataType;
import com.lchclearnet.market.FxMarket;
import com.lchclearnet.market.SourceType;

import com.lchclearnet.request.AbstractSmartService;
import com.lchclearnet.request.SmartRequest;
import com.lchclearnet.request.SmartService;

import com.lchclearnet.utils.Tuple;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public class DataFrameMarketAdapter extends AbstractSmartService<FxMarket> implements SmartService<SmartRequest, FxMarket> {

    private final AppConfig config;
    private final ConcurrentMap<Tuple, DataFrame> data;

    public DataFrameMarketAdapter(AppConfig config) {
        this.config = config;
        this.data = new ConcurrentHashMap<>();
    }

    public DataFrameFxMarket submit(SmartRequest request) {

        String types = request.get("types");
        Iterable<DataType> mktDataTypes = Arrays.stream(types.split(",")).map(t -> DataType.valueOf(t.trim())).collect(Collectors.toSet());

        Map<SourceType, Object> dataContext = new HashMap<>();
        for (DataType mktDataType : mktDataTypes) {
            for (SourceType dependent : mktDataType.getDependencies()) {
                dataContext.put(dependent, data.computeIfAbsent(Tuple.of(request.exportParams("types"), dependent), this::load));
            }
        }

        return new DataFrameFxMarket(mktDataTypes, dataContext);
    }

    private DataFrame load(Tuple key) {

        Map<String, Object> req = key.item(0);
        SourceType type = key.item(1);
        String marketDir = (String) req.getOrDefault("market_dir", config.get(AppConfig.MARKET, AppConfig.DIR));

        DataFrameCsvReader reader;
        switch (type) {
            case FX_SPOT:
                reader = DataFrameCsvReader.of(config, AppConfig.MARKET, AppConfig.FX_SPOT);
                break;
            case FX_FORWARD:
                reader = DataFrameCsvReader.of(config, AppConfig.MARKET, AppConfig.FX_FORWARD);
                break;
            case IR_YIELD_CURVE:
                reader = DataFrameCsvReader.of(config, AppConfig.MARKET, AppConfig.IR_YIELD_CURVE);
                break;
            case FX_VOLATILITY:
                reader = DataFrameCsvReader.of(config, AppConfig.MARKET, AppConfig.FX_VOLATILITY);
                break;
            default:
                throw new RuntimeException(String.format("'%s' cannot be loaded because it is not supported.", String.valueOf(type)));
        }

        return reader.setDir(marketDir).read();
    }

}
