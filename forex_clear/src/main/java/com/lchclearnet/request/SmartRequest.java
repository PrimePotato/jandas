package com.lchclearnet.request;

import java.util.*;
import java.util.function.BiConsumer;

public class SmartRequest {

    private Map<String, Object> parameters;

    public SmartRequest() {
        parameters = new HashMap<>();
    }

    public boolean has(String param) {
        return parameters.containsKey(param);
    }

    public <T> T get(String param) {
        return get(param, null);
    }

    public <T> T get(String param, T defaultValue) {
        return (T) parameters.getOrDefault(param, defaultValue);
    }

    public SmartRequest put(String param, Object value) {
        parameters.put(param, value);
        return this;
    }

    public void putAll(Map<? extends String, ?> params) {
        parameters.putAll(params);
    }

    public Map<String, Object> exportParams(String... excludes) {
        List<String> excludeSet = Arrays.asList(excludes);
        Map<String, Object> params = new HashMap<>();
        parameters.entrySet()
                .stream().filter(e -> !excludeSet.contains(e.getKey()))
                .forEach(e -> params.put(e.getKey(), e.getValue()));

        return params;
    }

    public void forEach(BiConsumer<? super String, ? super Object> paramsConsumer) {
        parameters.forEach(paramsConsumer);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SmartRequest that = (SmartRequest) o;
        return Objects.equals(parameters, that.parameters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parameters);
    }
}
