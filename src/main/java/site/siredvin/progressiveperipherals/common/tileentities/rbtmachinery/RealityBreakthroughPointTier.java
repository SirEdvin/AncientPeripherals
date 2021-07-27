package site.siredvin.progressiveperipherals.common.tileentities.rbtmachinery;

import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.api.puzzles.IPuzzle;
import site.siredvin.progressiveperipherals.common.puzzles.LinearSystem;

import java.awt.*;
import java.util.Random;

// TODO: remake with config logic
public enum RealityBreakthroughPointTier {
    COMMON(200, 400, Color.BLACK, 4, new LinearSystem(4));

    private final int minPower;
    private final int maxPower;
    private final int encryptLevels;
    private final Color color;
    private final IPuzzle puzzle;

    RealityBreakthroughPointTier(int minPower, int maxPower, Color color, int encryptLevels, IPuzzle puzzle) {
        this.minPower = minPower;
        this.maxPower = maxPower;
        this.color = color;
        this.puzzle = puzzle;
        this.encryptLevels  = encryptLevels;
    }

    public int getMaxPower() {
        return maxPower;
    }

    public int getMinPower() {
        return minPower;
    }

    public @NotNull Color getColor() {
        return color;
    }

    public @NotNull IPuzzle getPuzzle() {
        return puzzle;
    }

    public int getEncryptLevels() {
        return encryptLevels;
    }

    public int getPowerLevel() {
        return getPowerLevel(new Random());
    }

    public int getPowerLevel(Random rand) {
        return rand.nextInt(maxPower - minPower) + minPower;
    }
}
