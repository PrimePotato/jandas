package com.lchclearnet.trade;

import com.lchclearnet.request.AbstractSmartService;
import com.lchclearnet.request.SmartRequest;
import com.lchclearnet.request.SmartService;

public class TradeServiceFactory extends AbstractSmartService<TradeService> {

    private final SmartService<SmartRequest, TradeService> accessor;

    public TradeServiceFactory(SmartService<SmartRequest, TradeService> accessor) {
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
    public TradeService submit(SmartRequest request) {
        return accessor.submit(request);
    }

}
