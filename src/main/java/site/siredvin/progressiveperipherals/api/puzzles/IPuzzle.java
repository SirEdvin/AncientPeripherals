package site.siredvin.progressiveperipherals.api.puzzles;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

public interface IPuzzle {
    @NotNull String getType();

    @NotNull String getDescription();

    @NotNull IPuzzleTask getNewTask(Random random);
}
