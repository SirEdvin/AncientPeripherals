package site.siredvin.progressiveperipherals.common.tileentities.rbtmachinery;

import java.awt.*;
import java.util.Random;

// TODO: remake with config logic
public enum RealityBreakthroughPointTier {
    COMMON(200, 400, Color.BLACK);

    private final int minPower;
    private final int maxPower;
    private final Color color;

    RealityBreakthroughPointTier(int minPower, int maxPower, Color color) {
        this.minPower = minPower;
        this.maxPower = maxPower;
        this.color = color;
    }

    public int getMaxPower() {
        return maxPower;
    }

    public int getMinPower() {
        return minPower;
    }

    public Color getColor() {
        return color;
    }

    public int getPowerLevel() {
        return getPowerLevel(new Random());
    }

    public int getPowerLevel(Random rand) {
        return rand.nextInt(maxPower - minPower) + minPower;
    }
}
