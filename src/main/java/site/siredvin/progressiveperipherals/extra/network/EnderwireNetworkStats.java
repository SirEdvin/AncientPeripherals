package site.siredvin.progressiveperipherals.extra.network;

import site.siredvin.progressiveperipherals.common.configuration.ProgressivePeripheralsConfig;
import site.siredvin.progressiveperipherals.extra.network.api.NetworkAmplifier;

import java.util.Objects;

public final class EnderwireNetworkStats {
    private final boolean interdimensional;
    private final int reachableRange;

    public EnderwireNetworkStats(boolean interdimensional, int reachableRange) {
        this.interdimensional = interdimensional;
        this.reachableRange = reachableRange;
    }

    public boolean isInterdimensional() {
        return interdimensional;
    }

    public int getReachableRange() {
        return reachableRange;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EnderwireNetworkStats)) return false;
        EnderwireNetworkStats that = (EnderwireNetworkStats) o;
        return interdimensional == that.interdimensional && reachableRange == that.reachableRange;
    }

    @Override
    public int hashCode() {
        return Objects.hash(interdimensional, reachableRange);
    }

    public static Builder build() {
        return new Builder();
    }

    public static class Builder {
        private boolean interdimensional;
        private int reachableRange;

        private Builder() {
            interdimensional = false;
            reachableRange = ProgressivePeripheralsConfig.enderwireNetworkRangeStep;
        }

        public void consumeAmplifier(NetworkAmplifier amplifier) {
            switch (amplifier) {
                case MAKE_INTERDIMENSIONAL:
                    interdimensional = true;
                    break;
                case EXTEND_RANGE:
                    reachableRange += ProgressivePeripheralsConfig.enderwireNetworkRangeStep;
                    break;
            }
        }

        public EnderwireNetworkStats finish() {
            return new EnderwireNetworkStats(interdimensional, reachableRange);
        }
    }
}
