package com.lchclearnet.mutator.morpheus;

import com.lchclearnet.market.MarketService;
import com.lchclearnet.market.morpheus.DataFrameMarketServiceFactory;
import com.lchclearnet.mutator.ScenarioService;

import java.util.HashMap;
import java.util.Map;

public class DataFrameScenarioService implements ScenarioService {

    private final Map<String, ScenarioService> serviceContext;

    public DataFrameScenarioService(final Iterable<String> scenarioNames) {

        this.serviceContext = new HashMap<>();
        DataFrameMarketServiceFactory mktFactory = new DataFrameMarketServiceFactory();
        //scenarioNames.forEach(scenarioName -> this.serviceContext.put(scenarioName, scenarioFactory.getScenarioService(scenarioName, serviceContext)));
    }

    @Override
    public MarketService apply(MarketService mktService, String... scenarioNames) {
        return serviceContext.get(scenarioNames[0]).apply(mktService);
    }
}
