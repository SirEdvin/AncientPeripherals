package site.siredvin.progressiveperipherals.common.blocks.enderwire;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.common.blocks.base.TileEntityBlock;
import site.siredvin.progressiveperipherals.extra.network.api.IEnderwireElement;
import site.siredvin.progressiveperipherals.extra.network.tools.NetworkElementTool;

public abstract class BaseEnderwireBlock<T extends TileEntity & IEnderwireElement<T>> extends TileEntityBlock<T> {

    public static final BooleanProperty CONNECTED = BooleanProperty.create("connected");

    public BaseEnderwireBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(generateDefaultState(this.getStateDefinition().any()));
    }

    public BlockState generateDefaultState(BlockState parentDefaultState) {
        return parentDefaultState.setValue(CONNECTED, false);
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(CONNECTED);
    }

    @Override
    public void setPlacedBy(@NotNull World world, @NotNull BlockPos pos, @NotNull BlockState state, @Nullable LivingEntity entity, @NotNull ItemStack stack) {
        super.setPlacedBy(world, pos, state, entity, stack);
        if (!world.isClientSide && entity instanceof PlayerEntity) {
            NetworkElementTool.handleNetworkSetup(Hand.OFF_HAND, (PlayerEntity) entity, (ServerWorld) world, pos);
        }
    }

    @Override
    public @NotNull ActionResultType use(@NotNull BlockState state, @NotNull World world, @NotNull BlockPos pos, @NotNull PlayerEntity playerEntity, @NotNull Hand hand, @NotNull BlockRayTraceResult hit) {
        ActionResultType handledUse = NetworkElementTool.handleUse(state, world, pos, playerEntity, hand, hit);
        if (handledUse != null)
            return handledUse;
        return super.use(state, world, pos, playerEntity, hand, hit);
    }
}
