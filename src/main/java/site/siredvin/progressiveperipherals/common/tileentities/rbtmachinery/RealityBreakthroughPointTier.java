package site.siredvin.progressiveperipherals.common.tileentities.rbtmachinery;

import de.srendi.advancedperipherals.common.addons.computercraft.base.IConfigHandler;
import net.minecraftforge.common.ForgeConfigSpec;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.api.puzzles.IPuzzle;
import site.siredvin.progressiveperipherals.common.puzzles.LinearSystem;

import java.awt.*;
import java.util.Random;

public enum RealityBreakthroughPointTier implements IConfigHandler {
    COMMON(200, 400, Color.BLACK, 4, new LinearSystem(4));

    private final int defaultMinPower;
    private final int defaultMaxPower;
    private final int defaultEncryptLevels;

    private ForgeConfigSpec.IntValue minPower;
    private ForgeConfigSpec.IntValue maxPower;
    private ForgeConfigSpec.IntValue encryptLevels;

    private final Color color;
    private final IPuzzle puzzle;

    RealityBreakthroughPointTier(int defaultMinPower, int defaultMaxPower, Color color, int encryptLevels, IPuzzle puzzle) {
        this.defaultMinPower = defaultMinPower;
        this.defaultMaxPower = defaultMaxPower;
        this.color = color;
        this.puzzle = puzzle;
        this.defaultEncryptLevels = encryptLevels;
    }

    public @NotNull Color getColor() {
        return color;
    }

    public @NotNull IPuzzle getPuzzle() {
        return puzzle;
    }

    public int getEncryptLevels() {
        return encryptLevels.get();
    }

    public int getPowerLevel() {
        return getPowerLevel(new Random());
    }

    public int getPowerLevel(Random rand) {
        return rand.nextInt(maxPower.get() - minPower.get()) + minPower.get();
    }

    @Override
    public String settingsPostfix() {
        return "PointTier";
    }

    @Override
    public void addToConfig(ForgeConfigSpec.Builder builder) {
        minPower = builder.defineInRange(settingsName() + "MinPower", defaultMinPower, 1, Integer.MAX_VALUE);
        maxPower = builder.defineInRange(settingsName() + "MaxPower", defaultMaxPower, 1, Integer.MAX_VALUE);
        encryptLevels = builder.defineInRange(settingsName() + "EncryptLevels", defaultEncryptLevels, 1, Integer.MAX_VALUE);
    }
}
