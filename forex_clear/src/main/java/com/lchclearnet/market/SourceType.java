package com.lchclearnet.market;

import com.google.common.collect.ImmutableSet;

import java.util.Collection;

public enum SourceType implements MarketDataType<SourceType> {

    FX_SPOT {
        @Override
        public Collection<SourceType> getDependencies() {
            return ImmutableSet.of(this);
        }
    },

    IR_YIELD_CURVE {
        @Override
        public Collection<SourceType> getDependencies() {
            return ImmutableSet.of(this);
        }
    },

    FX_FORWARD {
        @Override
        public Collection<SourceType> getDependencies() {
            return ImmutableSet.of(IR_YIELD_CURVE, this);
        }
    },

    FX_VOLATILITY {
        @Override
        public Collection<SourceType> getDependencies() {
            return ImmutableSet.of(this);
        }
    }

}