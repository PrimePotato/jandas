package com.lchclearnet.market;

import com.lchclearnet.request.AbstractSmartService;
import com.lchclearnet.request.SmartRequest;
import com.lchclearnet.request.SmartService;

public class MarketServiceProviderFactory extends AbstractSmartService<FxMarket> {

    private final SmartService<SmartRequest, FxMarket> accessor;

    public MarketServiceProviderFactory(SmartService<SmartRequest, FxMarket> accessor) {
        this.accessor = accessor;
    }

    @Override
    public void startup() {
        accessor.startup();
    }

    @Override
    public void shutdown() {
        accessor.shutdown();
    }

    @Override
    public FxMarket submit(SmartRequest request) {
        return accessor.submit(request);
    }

}
