package com.lchclearnet.request;

public interface SmartService<REQUEST extends SmartRequest, RESPONSE> {
    void startup();

    void shutdown();

    RESPONSE submit(SmartRequest REQUEST);
}
