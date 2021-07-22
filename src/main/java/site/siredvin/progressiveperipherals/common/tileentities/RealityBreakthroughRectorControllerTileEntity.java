package site.siredvin.progressiveperipherals.common.tileentities;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.api.integrations.IPeripheralPlugin;
import site.siredvin.progressiveperipherals.api.integrations.IPluggableLuaMethod;
import site.siredvin.progressiveperipherals.api.multiblock.ICubeMultiBlockController;
import site.siredvin.progressiveperipherals.api.multiblock.IMultiBlockStructure;
import site.siredvin.progressiveperipherals.common.multiblock.CubeMultiBlock;
import site.siredvin.progressiveperipherals.common.setup.Blocks;
import site.siredvin.progressiveperipherals.common.setup.TileEntityTypes;
import site.siredvin.progressiveperipherals.common.tags.BlockTags;
import site.siredvin.progressiveperipherals.common.tileentities.base.MutableNBTPeripheralTileEntity;
import site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.RealityBreakthroughReactorControllerPeripheral;
import site.siredvin.progressiveperipherals.integrations.computercraft.plugins.rbtreactor.ControllerPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class RealityBreakthroughRectorControllerTileEntity extends MutableNBTPeripheralTileEntity<RealityBreakthroughReactorControllerPeripheral> implements ICubeMultiBlockController {

    public final static int SIZE = 5;

    private final static String CONFIGURED_TAG = "configured";
    private final static String NORTH_WEST_LOWEST_POS_TAG = "northWestLowestPos";

    private final static Predicate<BlockState> CORNER_PREDICATE = state -> state.is(BlockTags.BREAKTHROUGH_REACTOR_CORNER);
    private final static Predicate<BlockState> CASING_PREDICATE = state -> state.is(BlockTags.BREAKTHROUGH_REACTOR_CASING);
    private final static Predicate<BlockState> CENTER_PREDICATE = state -> state.is(Blocks.REALITY_BREAKTHROUGH_POINT.get());
    private final static Predicate<BlockState> INSIDE_PREDICATE = AbstractBlock.AbstractBlockState::isAir;

    private boolean configured = false;
    private @Nullable CubeMultiBlock structure;
    private final List<IPeripheralPlugin<RealityBreakthroughRectorControllerTileEntity>> plugins = new ArrayList<>();
    private final Map<String, IPluggableLuaMethod<RealityBreakthroughRectorControllerTileEntity>> methodMap = new HashMap<>();
    private String[] methodNames;

    public RealityBreakthroughRectorControllerTileEntity() {
        super(TileEntityTypes.REALITY_BREAKTHROUGH_REACTOR_CONTROLLER.get());
    }

    @Override
    protected @NotNull RealityBreakthroughReactorControllerPeripheral createPeripheral() {
        return new RealityBreakthroughReactorControllerPeripheral("realityBreakthroughReactorController", this);
    }

    @NotNull
    public String[] getMethodNames() {
        return methodNames;
    }

    @Override
    public void setMethodNames(@NotNull String[] methodNames) {
        this.methodNames = methodNames;
    }

    @Override
    public @NotNull List<IPeripheralPlugin<RealityBreakthroughRectorControllerTileEntity>> getPlugins() {
        return plugins;
    }

    @Override
    public @NotNull Map<String, IPluggableLuaMethod<RealityBreakthroughRectorControllerTileEntity>> getMethodMap() {
        return methodMap;
    }

    @Override
    public void injectDefaultPlugins() {
        plugins.add(new ControllerPlugin());
    }

    @Override
    public CompoundNBT saveInternalData(CompoundNBT data) {
        data.putBoolean(CONFIGURED_TAG, configured);
        if (structure != null) {
            data.put(NORTH_WEST_LOWEST_POS_TAG, NBTUtil.writeBlockPos(structure.getNorthWestLowest().getPos()));
        }
        return data;
    }

    @Override
    public void loadInternalData(BlockState state, CompoundNBT data, boolean skipUpdate) {
        configured = data.getBoolean(CONFIGURED_TAG);
        if (data.contains(NORTH_WEST_LOWEST_POS_TAG)) {
            BlockPos northWestLowest = NBTUtil.readBlockPos(data.getCompound(NORTH_WEST_LOWEST_POS_TAG));
            structure = new CubeMultiBlock(northWestLowest, SIZE);
        }
    }

    @Override
    public RealityBreakthroughRectorControllerTileEntity getThis() {
        return this;
    }

    @Override
    public @Nullable IMultiBlockStructure getStructure() {
        return structure;
    }

    public void setStructure(@NotNull CubeMultiBlock structure) {
        this.structure = structure;
    }

    @Override
    public void setConfigured(boolean configured) {
        this.configured = configured;
    }

    public boolean isConfigured() {
        return configured;
    }

    @Override
    public Predicate<BlockState> getCasingPredicate() {
        return CASING_PREDICATE;
    }

    @Override
    public Predicate<BlockState> getCornerPredicate() {
        return CORNER_PREDICATE;
    }

    @Override
    public Predicate<BlockState> getCenterPredicate() {
        return CENTER_PREDICATE;
    }

    @Override
    public Predicate<BlockState> getInsidePredicate() {
        return INSIDE_PREDICATE;
    }

    @Override
    public int getSize() {
        return SIZE;
    }

    @Override
    public void deconstructionCallback() {
        configured = false;
    }
}
