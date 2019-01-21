package com.lchclearnet.market;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MarketRequest {

    private final Set<MarketKey> marketKeys;

    public MarketRequest(Set<MarketKey> marketKeys) {

        this.marketKeys = marketKeys;
    }

    public static MarketRequest merge(Collection<MarketRequest> otherRequests) {
        Stream<MarketKey> mktKeys = otherRequests.stream().flatMap(m -> m.marketKeys.stream()).distinct();
        return new MarketRequest(mktKeys.collect(Collectors.toSet()));
    }


}
