package com.lchclearnet.mutator;

import com.lchclearnet.market.MarketService;

public interface ScenarioService {
    MarketService apply(MarketService mktService, String... scenarioNames);
}
