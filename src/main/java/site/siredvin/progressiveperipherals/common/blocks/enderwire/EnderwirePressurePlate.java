package site.siredvin.progressiveperipherals.common.blocks.enderwire;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PressurePlateBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.common.tileentities.enderwire.EnderwireSensorTileEntity;
import site.siredvin.progressiveperipherals.extra.network.api.EnderwireNetworkComponent;
import site.siredvin.progressiveperipherals.extra.network.api.IEnderwireSensorBlock;
import site.siredvin.progressiveperipherals.extra.network.events.EnderwireNetworkProducer;
import site.siredvin.progressiveperipherals.extra.network.tools.NetworkElementTool;
import site.siredvin.progressiveperipherals.utils.BlockUtils;

import java.util.List;
import java.util.stream.Collectors;

public class EnderwirePressurePlate extends PressurePlateBlock implements IEnderwireSensorBlock {

    public static final BooleanProperty CONNECTED = BaseEnderwireBlock.CONNECTED;

    private final boolean verbose;

    public EnderwirePressurePlate(boolean verbose) {
        super(Sensitivity.EVERYTHING, BlockUtils.decoration());
        this.registerDefaultState(this.stateDefinition.any().setValue(CONNECTED, false).setValue(POWERED, false));
        this.verbose = verbose;
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(CONNECTED);
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        ActionResultType handledUse = NetworkElementTool.handleUse(state, world, pos, player, hand, hit);
        if (handledUse != null)
            return handledUse;
        return super.use(state, world, pos, player, hand, hit);
    }

    @Override
    public void setPlacedBy(@NotNull World world, @NotNull BlockPos pos, @NotNull BlockState state, @Nullable LivingEntity entity, @NotNull ItemStack stack) {
        super.setPlacedBy(world, pos, state, entity, stack);
        if (!world.isClientSide && entity instanceof PlayerEntity) {
            NetworkElementTool.handleNetworkSetup(Hand.OFF_HAND, (PlayerEntity) entity, (ServerWorld) world, pos);
        }
    }

    @Override
    public void onRemove(BlockState state, World world, BlockPos blockPos, BlockState newState, boolean isMoving) {
        if (state.getValue(POWERED) && newState.is(this) && !newState.getValue(POWERED))
            EnderwireNetworkProducer.firePoweredPlateEvent(false, world, blockPos, null, this.verbose);
        super.onRemove(state, world, blockPos, newState, isMoving);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    protected void collidingTrigger(World world, BlockPos pos, BlockState stateBeforeColliding) {
        if (!stateBeforeColliding.getValue(POWERED)) {
            AxisAlignedBB axisalignedbb = TOUCH_AABB.move(pos);
            List<? extends Entity> list;
            // Only works now with Sensitivity.EVERYTHING
            list = world.getEntities(null, axisalignedbb).stream().filter(entity -> !entity.isIgnoringBlockTriggers()).collect(Collectors.toList());

            if (!list.isEmpty())
                EnderwireNetworkProducer.firePoweredPlateEvent(true, world, pos, list, this.verbose);
        }
    }

    @Override
    protected void checkPressed(World world, BlockPos pos, BlockState state, int currentPower) {
        super.checkPressed(world, pos, state, currentPower);
        collidingTrigger(world, pos, state);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new EnderwireSensorTileEntity();
    }

    @Override
    public EnderwireNetworkComponent getComponentType() {
        return EnderwireNetworkComponent.PLATE;
    }

    public int getSignal(BlockState p_180656_1_, IBlockReader p_180656_2_, BlockPos p_180656_3_, Direction p_180656_4_) {
        return 0;
    }

    public int getDirectSignal(BlockState p_176211_1_, IBlockReader p_176211_2_, BlockPos p_176211_3_, Direction p_176211_4_) {
        return 0;
    }

    @Override
    public boolean isSignalSource(BlockState p_149744_1_) {
        return false;
    }
}
