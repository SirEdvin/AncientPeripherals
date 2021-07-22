package site.siredvin.progressiveperipherals.common.tileentities;

import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.peripheral.IComputerAccess;
import de.srendi.advancedperipherals.common.util.Pair;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.api.blocks.IPeripheralPlugin;
import site.siredvin.progressiveperipherals.api.integrations.IPluggableLuaMethod;
import site.siredvin.progressiveperipherals.api.multiblock.IMultiBlockStructure;
import site.siredvin.progressiveperipherals.api.tileentity.IMultiBlockController;
import site.siredvin.progressiveperipherals.common.multiblock.MultiBlockUtils;
import site.siredvin.progressiveperipherals.common.multiblock.SquareMultiBlock;
import site.siredvin.progressiveperipherals.common.setup.Blocks;
import site.siredvin.progressiveperipherals.common.setup.TileEntityTypes;
import site.siredvin.progressiveperipherals.common.tags.BlockTags;
import site.siredvin.progressiveperipherals.common.tileentities.base.MutableNBTPeripheralTileEntity;
import site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.RealityBreakthroughReactorControllerPeripheral;
import site.siredvin.progressiveperipherals.integrations.computercraft.plugins.rbtreactor.ControllerPlugin;
import site.siredvin.progressiveperipherals.utils.RepresentationUtils;

import java.util.*;
import java.util.function.Predicate;

public class RealityBreakthroughRectorControllerTileEntity extends MutableNBTPeripheralTileEntity<RealityBreakthroughReactorControllerPeripheral> implements IMultiBlockController {

    public final static int SIZE = 5;

    private final static String CONFIGURED_TAG = "configured";
    private final static String NORTH_WEST_LOWEST_POS_TAG = "northWestLowestPos";

    private final static Predicate<BlockState> CORNER_PREDICATE = state -> state.is(BlockTags.BREAKTHROUGH_REACTOR_CORNER);
    private final static Predicate<BlockState> CASING_PREDICATE = state -> state.is(BlockTags.BREAKTHROUGH_REACTOR_CASING);

    private boolean configured = false;
    private @Nullable SquareMultiBlock structure;
    private final List<IPeripheralPlugin<RealityBreakthroughRectorControllerTileEntity>> plugins = new ArrayList<>();
    private final Map<String, IPluggableLuaMethod<RealityBreakthroughRectorControllerTileEntity>> methodMap = new HashMap<>();
    private String[] methodNames;

    public RealityBreakthroughRectorControllerTileEntity() {
        super(TileEntityTypes.REALITY_BREAKTHROUGH_REACTOR_CONTROLLER.get());
        plugins.add(new ControllerPlugin());
        rebuildMethodMap();
    }

    protected void rebuildMethodMap() {
        methodMap.clear();
        for (IPeripheralPlugin<RealityBreakthroughRectorControllerTileEntity> plugin: plugins) {
            methodMap.putAll(plugin.getMethods());
        }
        methodNames = methodMap.keySet().toArray(new String[0]);
    }

    @Override
    protected @NotNull RealityBreakthroughReactorControllerPeripheral createPeripheral() {
        return new RealityBreakthroughReactorControllerPeripheral("realityBreakthroughReactorController", this);
    }

    @NotNull
    public String[] getMethodNames() {
        return methodNames;
    }

    @NotNull
    public MethodResult callMethod(@NotNull IComputerAccess access, @NotNull ILuaContext context, int methodIndex, @NotNull IArguments arguments) throws LuaException {
        IPluggableLuaMethod<RealityBreakthroughRectorControllerTileEntity> method = methodMap.get(methodNames[methodIndex]);
        if (method == null)
            throw new IllegalArgumentException("Cannot find method ...");
        return method.call(access, context, arguments, this);
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
            structure = new SquareMultiBlock(northWestLowest, SIZE);
        }
    }

    public Pair<Boolean, String> detectMultiBlock() {
        Objects.requireNonNull(level); // should never happen
        if (configured)
            return Pair.onlyLeft(true);
        BlockPos pos = getBlockPos();
        BlockPos lowestPoint = MultiBlockUtils.findLowestPoint(level, pos, CASING_PREDICATE, SIZE + 1);
        if (lowestPoint == null)
            return Pair.of(false, "Cannot find lowest point ...");
        Pair<BlockPos, BlockPos> corners = MultiBlockUtils.findCorners(level, lowestPoint, CORNER_PREDICATE, SIZE + 1);
        if (corners == null)
            return Pair.of(false, "Cannot find lowest corners ...");
        BlockPos northWestCorner = corners.getLeft();
        BlockPos southEastCorner = corners.getRight();
        int size = MultiBlockUtils.calculateSquare(northWestCorner, southEastCorner);
        if (size == -1)
            return Pair.of(false, "Floor level is not even square!");
        if (size != 5)
            return Pair.of(false, "Floor level should be 5x5 square");
        structure = new SquareMultiBlock(northWestCorner, southEastCorner);
        // Check controller position
        if (structure.isInside(pos))
            return Pair.of(false, "Controller shouldn't be inside");
        if (structure.isInsideUpOrDownSide(pos))
            return Pair.of(false, "Controller shouldn't be inside up or down side");
        // Check corners
        List<BlockPos> incorrectPlacedBlocks = new ArrayList<>();
        structure.traverseCorners(blockPos -> {
            BlockState trState = level.getBlockState(blockPos);
            if (!CORNER_PREDICATE.test(trState))
                incorrectPlacedBlocks.add(blockPos);
        });
        if (!incorrectPlacedBlocks.isEmpty())
            return Pair.of(false, String.format("Incorrect placed corners presents: %s", RepresentationUtils.mergeValues(incorrectPlacedBlocks)));
        // Check internals
        structure.traverseInsideSides(blockPos -> {
            BlockState trState = level.getBlockState(blockPos);
            if (!CASING_PREDICATE.test(trState))
                incorrectPlacedBlocks.add(blockPos);
        });
        if (!incorrectPlacedBlocks.isEmpty())
            return Pair.of(false, String.format("Incorrect placed blocks presents: %s", RepresentationUtils.mergeValues(incorrectPlacedBlocks)));
        // Check is point present
        BlockPos center = structure.getCenter();
        if (!level.getBlockState(center).is(Blocks.REALITY_BREAKTHROUGH_POINT.get()))
            return Pair.of(false, "Reality breakthrough point should be inside");
        structure.traverseInside(blockPos -> {
            if (!blockPos.equals(center) && !level.isEmptyBlock(blockPos))
                incorrectPlacedBlocks.add(blockPos);
        });
        if (!incorrectPlacedBlocks.isEmpty())
            return Pair.of(false, String.format("This blocks should be empty: %s", RepresentationUtils.mergeValues(incorrectPlacedBlocks)));
        structure.setupFacingAndConnections(level, pos);
        configured = true;
        return Pair.onlyLeft(true);
    }

    @Override
    public @Nullable IMultiBlockStructure getStructure() {
        return structure;
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
    public void deconstructionCallback() {
        configured = false;
    }
}
