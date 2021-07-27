package site.siredvin.progressiveperipherals.common.puzzles;

import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.api.puzzles.IPuzzle;

import java.util.Random;

public class LinearSystem implements IPuzzle {

    private final int size;

    public LinearSystem(int size) {
        this.size = size;
    }

    @Override
    public @NotNull String getType() {
        return "linear system";
    }

    @Override
    public @NotNull String getDescription() {
        return "Simple linear system, that always have one solution";
    }

    @Override
    public @NotNull LinearSystemDescription getNewTask(Random random) {
        return LinearSystemDescription.generate(size, random);
    }
}
