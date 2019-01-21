package com.lchclearnet.request;

public abstract class AbstractSmartService<REP> implements SmartService<SmartRequest, REP> {

    @Override
    public void startup() {
    }

    @Override
    public void shutdown() {
    }
}
