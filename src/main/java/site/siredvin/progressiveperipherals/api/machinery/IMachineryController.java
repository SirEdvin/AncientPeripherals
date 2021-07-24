package site.siredvin.progressiveperipherals.api.machinery;

import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.peripheral.IComputerAccess;
import de.srendi.advancedperipherals.common.util.Pair;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.api.integrations.IPeripheralPlugin;
import site.siredvin.progressiveperipherals.api.integrations.IPluggableLuaMethod;
import site.siredvin.progressiveperipherals.common.machinery.MachineryBlockProperties;
import site.siredvin.progressiveperipherals.utils.ValueContainer;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

public interface IMachineryController<T extends TileEntity & IMachineryController<T>> {
    // strange default logic
    T getThis();
    // multi-block structure logic
    Pair<Boolean, String> detectMultiBlock();
    @Nullable IMachineryStructure getStructure();
    @Nullable World getLevel();
    BlockPos getBlockPos();
    boolean isConfigured();
    Predicate<BlockState> getCasingPredicate();
    Predicate<BlockState> getCornerPredicate();
    Predicate<BlockState> getCenterPredicate();
    Predicate<BlockState> getInsidePredicate();

    default boolean isBelongTo(BlockPos pos) {
        IMachineryStructure structure = getStructure();
        if (structure == null)
            return false;
        return structure.isBelongTo(pos);
    }

    default void deconstructMultiBlock() {
        World world = getLevel();
        Objects.requireNonNull(world); // should never happen
        if (!isConfigured())
            return;
        IMachineryStructure structure = getStructure();
        if (structure == null)
            return;
        Predicate<BlockState> casingPredicate = getCasingPredicate();
        Predicate<BlockState> cornerPredicate = getCornerPredicate();
        structure.traverseCorners(blockPos -> {
            BlockState state = world.getBlockState(blockPos);
            if (cornerPredicate.test(state))
                world.setBlockAndUpdate(blockPos, MachineryBlockProperties.setConnected(state, false));
        });
        structure.traverseInsideSides(blockPos -> {
            BlockState state = world.getBlockState(blockPos);
            if (casingPredicate.test(state))
                world.setBlockAndUpdate(blockPos, MachineryBlockProperties.setConnected(state, false));
        });
        commonDeconstruction();
    }

    // multi-block peripheral logic

    @NotNull String[] getMethodNames();
    void setMethodNames(@NotNull String[] value);
    @NotNull List<IPeripheralPlugin<T>> getPlugins();
    @NotNull Map<String, IPluggableLuaMethod<T>> getMethodMap();
    void injectDefaultPlugins();


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
        IMachineryStructure structure = getStructure();
        Objects.requireNonNull(structure);
        Objects.requireNonNull(world);
        List<IPeripheralPlugin<T>> plugins = getPlugins();
        plugins.clear();
        injectDefaultPlugins();
        structure.traverseInsideSides(blockPos -> {
            BlockState state = world.getBlockState(blockPos);
            if (state.getBlock() instanceof IMachineryPlugin) {
                IMachineryPlugin<T> pluginBlock = (IMachineryPlugin<T>) state.getBlock();
                IPeripheralPlugin<T> plugin = pluginBlock.getPlugin();
                plugins.add(plugin);
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

    // Advanced utility methods
    default ItemStack storeItem(ItemStack stack, boolean allowDrop) {
        IMachineryStructure structure = getStructure();
        if (structure == null)
            return stack;
        World world = getLevel();
        Objects.requireNonNull(world);
        ValueContainer<ItemStack> container = new ValueContainer<>(stack);
        structure.traverseInsideSides(blockPos -> {
            if (container.getValue().isEmpty())
                return;
            TileEntity tileEntity = world.getBlockEntity(blockPos);
            if (tileEntity != null) {
                tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(
                        handler -> container.setValue(ItemHandlerHelper.insertItemStacked(handler, container.getValue(), false))
                );
            }
        });
        BlockPos pos = getBlockPos();
        if (allowDrop && !container.getValue().isEmpty()) {
            InventoryHelper.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), container.getValue());
            return ItemStack.EMPTY;
        }
        return container.getValue();
    }

    default NonNullList<BlockPos> detectStorages() {
        IMachineryStructure structure = getStructure();
        if (structure == null)
            return NonNullList.create();
        World world = getLevel();
        Objects.requireNonNull(world);
        NonNullList<BlockPos> list = NonNullList.create();
        structure.traverseInsideSides(blockPos -> {
            TileEntity tileEntity = world.getBlockEntity(blockPos);
            if (tileEntity != null) {
                tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> list.add(blockPos));
            }
        });
        return list;
    }
}
