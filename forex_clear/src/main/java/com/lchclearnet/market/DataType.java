package com.lchclearnet.market;

import com.google.common.collect.ImmutableSet;

import java.util.Collection;

public enum DataType implements MarketDataType<SourceType> {

    IR_YIELD_CURVE {
        @Override
        public Collection<SourceType> getDependencies() {
            return ImmutableSet.of(SourceType.IR_YIELD_CURVE);
        }
    },

    FX_SPOT {
        @Override
        public Collection<SourceType> getDependencies() {
            return ImmutableSet.of(SourceType.FX_SPOT);
        }
    },

    FX_FORWARD_POINTS {
        @Override
        public Collection<SourceType> getDependencies() {
            return ImmutableSet.of(SourceType.FX_FORWARD);
        }
    },

    FX_FORWARD_OUTRIGHT {
        @Override
        public Collection<SourceType> getDependencies() {
            return ImmutableSet.of(SourceType.FX_SPOT, SourceType.FX_FORWARD);
        }
    },

    FX_VOLATILITY_SURFACE {
        @Override
        public Collection<SourceType> getDependencies() {
            return ImmutableSet.of(SourceType.FX_SPOT, SourceType.FX_FORWARD, SourceType.FX_VOLATILITY);
        }
    };
}