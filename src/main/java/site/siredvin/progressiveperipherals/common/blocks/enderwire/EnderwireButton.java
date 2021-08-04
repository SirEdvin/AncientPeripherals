package site.siredvin.progressiveperipherals.common.blocks.enderwire;

import net.minecraft.block.AbstractButtonBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.common.tileentities.enderwire.EnderwireSensorTileEntity;
import site.siredvin.progressiveperipherals.extra.network.api.EnderwireElementType;
import site.siredvin.progressiveperipherals.extra.network.api.IEnderwireSensorBlock;
import site.siredvin.progressiveperipherals.extra.network.events.EnderwireNetworkProducer;
import site.siredvin.progressiveperipherals.extra.network.tools.NetworkElementTool;
import site.siredvin.progressiveperipherals.utils.BlockUtils;

import static site.siredvin.progressiveperipherals.common.blocks.enderwire.BaseEnderwireBlock.CONNECTED;

public class EnderwireButton extends AbstractButtonBlock implements IEnderwireSensorBlock {

    private final boolean verbose;

    public EnderwireButton(boolean verbose) {
        super(false, BlockUtils.decoration());
        this.registerDefaultState(this.stateDefinition.any().setValue(CONNECTED, false).setValue(POWERED, false));
        this.verbose = verbose;
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(CONNECTED);
    }

    @Override
    public void onRemove(BlockState state, World world, BlockPos blockPos, BlockState newState, boolean isMoving) {
        if (state.getValue(POWERED) && newState.is(this) && !newState.getValue(POWERED))
            EnderwireNetworkProducer.firePoweredButtonEvent(false, world, blockPos, null, this.verbose);
        if (!newState.is(this)) // new block are not this block
            NetworkElementTool.handleRemove(world, blockPos);
        super.onRemove(state, world, blockPos, newState, isMoving);
    }

    @Override
    public void setPlacedBy(@NotNull World world, @NotNull BlockPos pos, @NotNull BlockState state, @Nullable LivingEntity entity, @NotNull ItemStack stack) {
        super.setPlacedBy(world, pos, state, entity, stack);
        if (!world.isClientSide && entity instanceof PlayerEntity) {
            NetworkElementTool.handleNetworkSetup(Hand.OFF_HAND, (PlayerEntity) entity, (ServerWorld) world, pos);
        }
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        ActionResultType handledUse = NetworkElementTool.handleUse(state, world, pos, player, hand, hit);
        if (handledUse != null)
            return handledUse;
        if (state.getValue(POWERED)) {
            return ActionResultType.CONSUME;
        } else {
            if (!world.isClientSide)
                EnderwireNetworkProducer.firePoweredButtonEvent(true, world, pos, player, this.verbose);
            this.press(state, world, pos);
            this.playSound(player, world, pos, true);
            return ActionResultType.sidedSuccess(world.isClientSide);
        }
    }

    @Override
    protected SoundEvent getSound(boolean p_196369_1_) {
        return p_196369_1_ ? SoundEvents.STONE_BUTTON_CLICK_ON : SoundEvents.STONE_BUTTON_CLICK_OFF;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new EnderwireSensorTileEntity();
    }

    @Override
    public EnderwireElementType getComponentType() {
        return EnderwireElementType.BUTTON;
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
