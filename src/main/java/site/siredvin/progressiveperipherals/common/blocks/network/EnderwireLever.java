package site.siredvin.progressiveperipherals.common.blocks.network;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeverBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.common.tileentities.enderwire.EnderwireSensorTileEntity;
import site.siredvin.progressiveperipherals.extra.network.IEnderwireElement;
import site.siredvin.progressiveperipherals.extra.network.events.EnderwireComputerEvent;
import site.siredvin.progressiveperipherals.extra.network.events.NetworkEventTool;
import site.siredvin.progressiveperipherals.extra.network.tools.NetworkElementTool;

public class EnderwireLever extends LeverBlock {
    public EnderwireLever() {
        super(AbstractBlock.Properties.of(Material.DECORATION).noCollission().strength(0.5F).sound(SoundType.WOOD));
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        ActionResultType handledUse = NetworkElementTool.handleUse(state, world, pos, player, hand, hit);
        if (handledUse != null)
            return handledUse;
        ActionResultType superResult = super.use(state, world, pos, player, hand, hit);
        if (!world.isClientSide) {
            IEnderwireElement<?> te = (IEnderwireElement<?>) world.getBlockEntity(pos);
            if (te != null) {
                boolean leverState = state.cycle(POWERED).getValue(POWERED);
                String attachedNetwork = te.getAttachedNetwork();
                if (attachedNetwork != null) {
                    String eventName = "lever_enabled";
                    if (!leverState)
                        eventName = "lever_disabled";
                    NetworkEventTool.fireComputerEvent(attachedNetwork, EnderwireComputerEvent.of(eventName, te.getElementUUID().toString()));
                }
            }
        }
        return superResult;
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
}
