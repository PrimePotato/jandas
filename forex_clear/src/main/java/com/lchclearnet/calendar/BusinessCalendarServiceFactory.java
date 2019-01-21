package com.lchclearnet.calendar;

import com.lchclearnet.request.AbstractSmartService;
import com.lchclearnet.request.SmartRequest;
import com.lchclearnet.request.SmartService;

public class BusinessCalendarServiceFactory extends AbstractSmartService<BusinessCalendarService> {

    private final SmartService<SmartRequest, BusinessCalendarService> accessor;

    public BusinessCalendarServiceFactory(SmartService<SmartRequest, BusinessCalendarService> accessor) {
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
    public BusinessCalendarService submit(SmartRequest request) {
        return accessor.submit(request);
    }

}
