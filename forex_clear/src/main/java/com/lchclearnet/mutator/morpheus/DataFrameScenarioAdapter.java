package com.lchclearnet.mutator.morpheus;


import com.lchclearnet.fx.AppConfig;
import com.lchclearnet.request.AbstractSmartService;
import com.lchclearnet.request.SmartRequest;
import com.lchclearnet.request.SmartService;
import com.lchclearnet.utils.morpheus.CsvAccessor;
import com.zavtech.morpheus.frame.DataFrame;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Stream;

public class DataFrameScenarioAdapter extends AbstractSmartService<DataFrameScenarioService> implements SmartService<SmartRequest, DataFrameScenarioService> {

    private final AppConfig config;
    private final ConcurrentMap<String, DataFrame<Integer, String>> data;

    public DataFrameScenarioAdapter(AppConfig config) {
        this.config = config;
        this.data = new ConcurrentHashMap<>();
    }

    public DataFrameScenarioService submit(SmartRequest request) {

        String scenarioDir = request.get("scenario_dir", config.get(AppConfig.SCENARIO, AppConfig.DIR));
        String scenariosStr = request.get("scenarios");

        Stream<String> scenarios;
        if (scenariosStr == null || scenariosStr.trim().length() == 0) {
            scenarios = ((Map) config.get(AppConfig.SCENARIO)).keySet().stream();
        } else {
            scenarios = Arrays.stream(scenariosStr.split(",")).distinct();
        }

        //parse the different scenario type from the request
        scenarios.forEach(scenario -> {
            data.put(scenario, load(scenarioDir, scenario));
        });

        return null;//new DataFrameScenarioService(scenario, scenarioContext);
    }

    private DataFrame<Integer, String> load(String dir, String scenario) {

        CsvAccessor<Integer> accessor;
        switch (scenario) {
            case AppConfig.FX_SPOT:
                accessor = CsvAccessor.of(config, AppConfig.SCENARIO, AppConfig.FX_SPOT);
                break;
            case AppConfig.IR_YIELD_CURVE:
                accessor = CsvAccessor.of(config, AppConfig.SCENARIO, AppConfig.IR_YIELD_CURVE);
                break;
            case AppConfig.FX_VOLATILITY:
                accessor = CsvAccessor.of(config, AppConfig.SCENARIO, AppConfig.FX_VOLATILITY);
                break;
            default:
                throw new RuntimeException(String.format("'%s' cannot be loaded because it is not supported.", String.valueOf(scenario)));
        }

        return accessor.setDir(dir).load();
    }

}
