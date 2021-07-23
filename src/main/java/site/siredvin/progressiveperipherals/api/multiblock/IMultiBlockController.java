package site.siredvin.progressiveperipherals.api.multiblock;

import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.peripheral.IComputerAccess;
import de.srendi.advancedperipherals.common.util.Pair;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.api.integrations.IPeripheralPlugin;
import site.siredvin.progressiveperipherals.api.integrations.IPluggableLuaMethod;
import site.siredvin.progressiveperipherals.common.multiblock.MultiBlockPropertiesUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

public interface IMultiBlockController<T extends TileEntity & IMultiBlockController<T>> {
    // strange default logic
    T getThis();
    // multi-block structure logic
    Pair<Boolean, String> detectMultiBlock();
    @Nullable IMultiBlockStructure getStructure();
    @Nullable World getLevel();
    BlockPos getBlockPos();
    boolean isConfigured();
    Predicate<BlockState> getCasingPredicate();
    Predicate<BlockState> getCornerPredicate();
    Predicate<BlockState> getCenterPredicate();
    Predicate<BlockState> getInsidePredicate();

    default void deconstructMultiBlock() {
        World world = getLevel();
        Objects.requireNonNull(world); // should never happen
        if (!isConfigured())
            return;
        IMultiBlockStructure structure = getStructure();
        if (structure == null)
            return;
        Predicate<BlockState> casingPredicate = getCasingPredicate();
        Predicate<BlockState> cornerPredicate = getCornerPredicate();
        structure.traverseCorners(blockPos -> {
            BlockState state = world.getBlockState(blockPos);
            if (cornerPredicate.test(state))
                world.setBlockAndUpdate(blockPos, MultiBlockPropertiesUtils.setConnected(state, false));
        });
        structure.traverseInsideSides(blockPos -> {
            BlockState state = world.getBlockState(blockPos);
            if (casingPredicate.test(state))
                world.setBlockAndUpdate(blockPos, MultiBlockPropertiesUtils.setConnected(state, false));
        });
        commonDeconstruction();
    }

    // multi-block peripheral logic

    @NotNull String[] getMethodNames();
    void setMethodNames(@NotNull String[] value);
    @NotNull List<IPeripheralPlugin<T>> getPlugins();
    @NotNull Map<String, IPluggableLuaMethod<T>> getMethodMap();
    void injectDefaultPlugins();
    void handleFeature(MultiBlockPluggableFeature feature);


    default void cleanPluginsAndMethods() {
        getPlugins().clear();
        getMethodMap().clear();
    }

    default @NotNull MethodResult callMethod(@NotNull IComputerAccess access, @NotNull ILuaContext context, int methodIndex, @NotNull IArguments arguments) throws LuaException {
        IPluggableLuaMethod<T> method = getMethodMap().get(getMethodNames()[methodIndex]);
        if (method == null)
            throw new IllegalArgumentException("Cannot find method ...");
        return method.call(access, context, arguments, getThis());
    }

    default void rebuildPluginsAndMethods() {
        World world = getLevel();
        IMultiBlockStructure structure = getStructure();
        Objects.requireNonNull(structure);
        Objects.requireNonNull(world);
        List<IPeripheralPlugin<T>> plugins = getPlugins();
        plugins.clear();
        injectDefaultPlugins();
        structure.traverseInsideSides(blockPos -> {
            BlockState state = world.getBlockState(blockPos);
            if (state.getBlock() instanceof IMultiBlockPlugin) {
                IMultiBlockPlugin<T> pluginBlock = (IMultiBlockPlugin<T>) state.getBlock();
                IPeripheralPlugin<T> plugin = pluginBlock.getPlugin();
                MultiBlockPluggableFeature feature = pluginBlock.getFeature();
                if (plugin != null)
                    plugins.add(plugin);
                if (feature != null)
                    handleFeature(feature);
            }
        });
        Map<String, IPluggableLuaMethod<T>> methodMap = getMethodMap();
        methodMap.clear();
        for (IPeripheralPlugin<T> plugin: plugins) {
            methodMap.putAll(plugin.getMethods());
        }
        setMethodNames(methodMap.keySet().toArray(new String[0]));
    }

     // internal logic

    default void commonDeconstruction() {
        cleanPluginsAndMethods();
        deconstructionCallback();
    }

    default void commonDetect() {
        rebuildPluginsAndMethods();
        detectCallback();
    }

    void deconstructionCallback();
    void detectCallback();
}
