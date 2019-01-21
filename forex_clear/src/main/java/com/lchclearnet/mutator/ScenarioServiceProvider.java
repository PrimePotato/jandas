package com.lchclearnet.mutator;

import com.lchclearnet.market.FxMarket;

public interface ScenarioServiceProvider {
    FxMarket apply(FxMarket mktService, String... scenarioNames);
}
