package com.lchclearnet.market.morpheus;


import com.lchclearnet.fx.AppConfig;
import com.lchclearnet.market.DataType;
import com.lchclearnet.market.FxMarket;
import com.lchclearnet.market.SourceType;
import com.lchclearnet.request.AbstractSmartService;
import com.lchclearnet.request.SmartRequest;
import com.lchclearnet.request.SmartService;
import com.lchclearnet.utils.morpheus.CsvAccessor;
import com.zavtech.morpheus.frame.DataFrame;
import com.lchclearnet.utils.Tuple;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public class DataFrameMarketAdapter extends AbstractSmartService<FxMarket> implements SmartService<SmartRequest, FxMarket> {

    private final AppConfig config;
    private final ConcurrentMap<Tuple, DataFrame<Integer, String>> data;

    public DataFrameMarketAdapter(AppConfig config) {
        this.config = config;
        this.data = new ConcurrentHashMap<>();
    }

    public DataFrameFxMarket submit(SmartRequest request) {

        String types = request.get("types");

        //parse the different market jandas type from the request
        //the jandas type are supposed to be final jandas type and not source type
        //the source types should be inferred by the dependency relationship defined in the DataType enum interface
        Iterable<DataType> mktDataTypes = Arrays.stream(types.split(",")).map(t -> DataType.valueOf(t.trim())).collect(Collectors.toSet());

        //preload the different source types that the requested jandas type should be built upon
        Map<SourceType, Object> dataContext = new HashMap<>();
        for (DataType mktDataType : mktDataTypes) {
            for (SourceType dependent : mktDataType.getDependencies()) {
                dataContext.put(dependent, data.computeIfAbsent(Tuple.of(request.exportParams("types"), dependent), this::load));
            }
        }

        return new DataFrameFxMarket(mktDataTypes, dataContext);
    }

    private DataFrame<Integer, String> load(Tuple key) {

        Map<String, Object> req = key.item(0);
        SourceType type = key.item(1);
        String marketDir = (String) req.getOrDefault("market_dir", config.get(AppConfig.MARKET, AppConfig.DIR));

        CsvAccessor<Integer> accessor;
        switch (type) {
            case FX_SPOT:
                accessor = CsvAccessor.of(config, AppConfig.MARKET, AppConfig.FX_SPOT);
                break;
            case FX_FORWARD:
                accessor = CsvAccessor.of(config, AppConfig.MARKET, AppConfig.FX_FORWARD);
                break;
            case IR_YIELD_CURVE:
                accessor = CsvAccessor.of(config, AppConfig.MARKET, AppConfig.IR_YIELD_CURVE);
                break;
            case FX_VOLATILITY:
                accessor = CsvAccessor.of(config, AppConfig.MARKET, AppConfig.FX_VOLATILITY);
                break;
            default:
                throw new RuntimeException(String.format("'%s' cannot be loaded because it is not supported.", String.valueOf(type)));
        }

        return accessor.setDir(marketDir).load();
    }

}
