package site.siredvin.progressiveperipherals.integrations.computercraft.plugins.machinery;

import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.peripheral.IComputerAccess;
import net.minecraft.tileentity.TileEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.api.machinery.IMachineryController;
import site.siredvin.progressiveperipherals.api.puzzles.IPuzzleTask;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class GeneralBreakthroughPointPlugin<T extends TileEntity & IMachineryController<T>> extends BreakthroughPointPlugin<T> {

    private @Nullable IPuzzleTask currentDecryptTask = null;
    private long startTimestamp = -1;

    @Override
    public void buildMethodMap() {
        methods.put("isDecrypted", this::isDecrypted);
        methods.put("getNewDecryptTask", this::getNewDecryptTask);
        methods.put("getDecryptDescription", this::getDecryptDescription);
        methods.put("checkSolution", this::checkSolution);
        methods.put("getPowerLevel", this::getPowerLevel);
    }

    public @NotNull MethodResult getPowerLevel(@NotNull IComputerAccess access, @NotNull ILuaContext context, @NotNull IArguments arguments, @NotNull T controllerEntity) throws LuaException {
        return withBreakthroughPoint(context, controllerEntity, (point, world) -> new Object[]{point.getPowerLevel()});
    }

    public @NotNull MethodResult isDecrypted(@NotNull IComputerAccess access, @NotNull ILuaContext context, @NotNull IArguments arguments, @NotNull T controllerEntity) throws LuaException {
        return withBreakthroughPoint(context, controllerEntity, (point, world) -> new Object[]{point.isDecrypted()});
    }

    public @NotNull MethodResult getDecryptDescription(@NotNull IComputerAccess access, @NotNull ILuaContext context, @NotNull IArguments arguments, @NotNull T controllerEntity)  throws LuaException {
        return withBreakthroughPoint(context, controllerEntity, (point, world) -> new Object[]{point.getPuzzle().getDescription()});
    }

    public @NotNull MethodResult getNewDecryptTask(@NotNull IComputerAccess access, @NotNull ILuaContext context, @NotNull IArguments arguments, @NotNull T controllerEntity) throws LuaException {
        return withBreakthroughPoint(context, controllerEntity, (point, world) -> {
            if (currentDecryptTask != null) {
                long difference = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) - startTimestamp;
                if (difference <= currentDecryptTask.getTimeLimitInSeconds())
                    return new Object[]{null, "You already has task"};
                currentDecryptTask = null;
            }
            if (point.isDecrypted())
                return new Object[]{null, "Point already decrypted"};
            currentDecryptTask = point.getPuzzle().getNewTask(world.getRandom());
            startTimestamp = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
            return new Object[]{currentDecryptTask.getLuaDescription()};
        });
    }

    public @NotNull MethodResult checkSolution(@NotNull IComputerAccess access, @NotNull ILuaContext context, @NotNull IArguments arguments, @NotNull T controllerEntity) throws LuaException {
        return withBreakthroughPoint(context, controllerEntity, (point, world) -> {
            if (currentDecryptTask == null)
                return new Object[]{null, "Please, take task first"};
            long difference = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) - startTimestamp;
            if (difference > currentDecryptTask.getTimeLimitInSeconds()) {
                currentDecryptTask = null;
                return new Object[]{null, "Time for solving is expired"};
            }
            boolean solutionResult;
            try {
                solutionResult = currentDecryptTask.checkSolution(arguments);
            } catch (LuaException e) {
                currentDecryptTask = null;
                return new Object[]{null, e.getMessage()};
            }
            currentDecryptTask = null;
            if (!solutionResult) {
                return new Object[]{null, "Incorrect answer"};
            }
            point.decryptLevel();
            return new Object[]{true};
        });
    }
}
