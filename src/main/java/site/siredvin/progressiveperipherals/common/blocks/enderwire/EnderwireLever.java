package site.siredvin.progressiveperipherals.common.blocks.enderwire;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.common.tileentities.enderwire.EnderwireSensorTileEntity;
import site.siredvin.progressiveperipherals.extra.network.api.EnderwireElementType;
import site.siredvin.progressiveperipherals.extra.network.api.IEnderwireSensorBlock;
import site.siredvin.progressiveperipherals.extra.network.events.EnderwireNetworkProducer;
import site.siredvin.progressiveperipherals.extra.network.tools.NetworkElementTool;

import java.util.Random;

import static site.siredvin.progressiveperipherals.common.blocks.enderwire.BaseEnderwireBlock.CONNECTED;

public class EnderwireLever extends LeverBlock implements IEnderwireSensorBlock {

    private final boolean verbose;

    private static void makeParticle(BlockState p_196379_0_, IWorld p_196379_1_, BlockPos p_196379_2_, float p_196379_3_) {
        Direction direction = p_196379_0_.getValue(FACING).getOpposite();
        Direction direction1 = getConnectedDirection(p_196379_0_).getOpposite();
        double d0 = (double)p_196379_2_.getX() + 0.5D + 0.1D * (double)direction.getStepX() + 0.2D * (double)direction1.getStepX();
        double d1 = (double)p_196379_2_.getY() + 0.5D + 0.1D * (double)direction.getStepY() + 0.2D * (double)direction1.getStepY();
        double d2 = (double)p_196379_2_.getZ() + 0.5D + 0.1D * (double)direction.getStepZ() + 0.2D * (double)direction1.getStepZ();
        p_196379_1_.addParticle(new RedstoneParticleData(0.65F, 0.4F, 0.21F, p_196379_3_), d0, d1, d2, 0.0D, 0.0D, 0.0D);
    }

    public EnderwireLever(boolean verbose) {
        super(AbstractBlock.Properties.of(Material.DECORATION).noCollission().strength(0.5F).sound(SoundType.WOOD));
        this.registerDefaultState(this.stateDefinition.any().setValue(CONNECTED, false).setValue(POWERED, false));
        this.verbose = verbose;
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(CONNECTED);
    }

    public ActionResultType overwrittenOriginalUse(BlockState state, World world, BlockPos pos, PlayerEntity player) {
        if (world.isClientSide) {
            BlockState blockstate1 = state.cycle(POWERED);
            if (blockstate1.getValue(POWERED)) {
                makeParticle(blockstate1, world, pos, 1.0F);
            }
            return ActionResultType.SUCCESS;
        }
        BlockState blockstate = this.pull(state, world, pos);
        EnderwireNetworkProducer.firePoweredLeverEvent(blockstate.getValue(POWERED), world, pos, player, verbose);
        float f = blockstate.getValue(POWERED) ? 0.6F : 0.5F;
        world.playSound(null, pos, SoundEvents.LEVER_CLICK, SoundCategory.BLOCKS, 0.3F, f);
        return ActionResultType.CONSUME;
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
        return overwrittenOriginalUse(state, world, pos, player);
    }

    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState p_180655_1_, World p_180655_2_, BlockPos p_180655_3_, Random p_180655_4_) {
        if (p_180655_1_.getValue(POWERED) && p_180655_4_.nextFloat() < 0.25F) {
            makeParticle(p_180655_1_, p_180655_2_, p_180655_3_, 0.5F);
        }
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
        return EnderwireElementType.LEVER;
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
