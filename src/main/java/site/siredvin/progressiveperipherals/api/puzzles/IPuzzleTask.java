package site.siredvin.progressiveperipherals.api.puzzles;

import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.LuaException;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public interface IPuzzleTask {
    @NotNull Map<String, Object> getLuaDescription();
    boolean checkSolution(@NotNull IArguments arguments) throws LuaException;
    int getTimeLimitInSeconds();
}
