package site.siredvin.progressiveperipherals.api.puzzles;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

public interface IPuzzle {
    @SuppressWarnings({"SameReturnValue", "unused"})
    @NotNull String getType();

    @SuppressWarnings("SameReturnValue")
    @NotNull String getDescription();

    @NotNull IPuzzleTask getNewTask(Random random);
}
