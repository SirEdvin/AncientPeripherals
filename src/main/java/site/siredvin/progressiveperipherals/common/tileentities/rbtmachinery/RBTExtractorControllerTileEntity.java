package site.siredvin.progressiveperipherals.common.tileentities.rbtmachinery;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.api.integrations.IPeripheralPlugin;
import site.siredvin.progressiveperipherals.api.machinery.ICubeMachineryController;
import site.siredvin.progressiveperipherals.api.machinery.IMachineryStructure;
import site.siredvin.progressiveperipherals.common.machinery.CubeMachineryStructure;
import site.siredvin.progressiveperipherals.common.setup.Blocks;
import site.siredvin.progressiveperipherals.common.setup.TileEntityTypes;
import site.siredvin.progressiveperipherals.common.tags.BlockTags;
import site.siredvin.progressiveperipherals.common.tileentities.base.MutableNBTPeripheralTileEntity;
import site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.machinery.RBTExtractorPeripheral;
import site.siredvin.progressiveperipherals.integrations.computercraft.plugins.machinery.GeneralBreakthroughPointPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class RBTExtractorControllerTileEntity extends MutableNBTPeripheralTileEntity<RBTExtractorPeripheral> implements ICubeMachineryController<RBTExtractorControllerTileEntity> {
    public final static int SIZE = 3;

    private final static String NORTH_WEST_LOWEST_POS_TAG = "northWestLowestPos";

    private final static Predicate<BlockState> CORNER_PREDICATE = state -> state.is(BlockTags.IRREALIUM_STRUCTURE_CORNER);
    private final static Predicate<BlockState> CASING_PREDICATE = state -> state.is(BlockTags.IRREALIUM_STRUCTURE_CASING);
    private final static Predicate<BlockState> CENTER_PREDICATE = state -> state.is(Blocks.REALITY_BREAKTHROUGH_POINT.get());
    private final static Predicate<BlockState> INSIDE_PREDICATE = state -> false;

    // actual state
    private boolean configured = false;
    private @Nullable CubeMachineryStructure structure;

    // peripheral logic
    private final List<IPeripheralPlugin<RBTExtractorControllerTileEntity>> plugins = new ArrayList<>();

    public RBTExtractorControllerTileEntity() {
        super(TileEntityTypes.REALITY_BREAKTHROUGH_EXTRACTOR_CONTROLLER.get());
    }

    @Override
    protected @NotNull RBTExtractorPeripheral createPeripheral() {
        return new RBTExtractorPeripheral("realityBreakthroughExtractorController", this);
    }

    @Override
    public @NotNull List<IPeripheralPlugin<RBTExtractorControllerTileEntity>> getPlugins() {
        return plugins;
    }


    @Override
    public void injectDefaultPlugins() {
        plugins.add(new GeneralBreakthroughPointPlugin<>());
    }

    @Override
    public void invalidateCapabilities() {
        invalidateCaps();
    }

    @Override
    public CompoundNBT saveInternalData(CompoundNBT data) {
        if (structure != null) {
            data.put(NORTH_WEST_LOWEST_POS_TAG, NBTUtil.writeBlockPos(structure.getNorthWestLowest().getPos()));
        }
        return data;
    }

    @Override
    public void loadInternalData(BlockState state, CompoundNBT data, boolean skipUpdate) {
        if (data.contains(NORTH_WEST_LOWEST_POS_TAG)) {
            BlockPos northWestLowest = NBTUtil.readBlockPos(data.getCompound(NORTH_WEST_LOWEST_POS_TAG));
            structure = new CubeMachineryStructure(northWestLowest, SIZE);
            if (level != null && !level.isClientSide)
                commonDetect();
        }
    }

    @Override
    public RBTExtractorControllerTileEntity getThis() {
        return this;
    }

    @Override
    public @Nullable IMachineryStructure getStructure() {
        return structure;
    }

    public void setStructure(@Nullable CubeMachineryStructure structure) {
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
    public void deconstructionCallback() {}

    @Override
    public void detectCallback() {}
}
