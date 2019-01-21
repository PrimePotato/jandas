package com.lchclearnet.mutator.morpheus;

import com.lchclearnet.market.FxMarket;
import com.lchclearnet.mutator.ScenarioService;
import com.lchclearnet.mutator.ScenarioServiceProvider;
import com.zavtech.morpheus.frame.DataFrame;

public class DataFrameScenarioServiceProvider implements ScenarioServiceProvider {

    private final ScenarioService fxSpotScenarioService;
    private final ScenarioService irYieldScenarioService;
    private final ScenarioService fxVolScenarioService;

    public DataFrameScenarioServiceProvider(DataFrame<Integer, String> data) {
        this.fxSpotScenarioService = new DataFrameFxSpotScenarioService(data);
        this.irYieldScenarioService = null;
        this.fxVolScenarioService = null;
    }

    @Override
    public FxMarket apply(FxMarket mktServiceProvider, String... scenarioNames) {
        return null;
    }
}
