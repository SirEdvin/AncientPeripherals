package site.siredvin.progressiveperipherals.common.events;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import site.siredvin.progressiveperipherals.common.machinery.MachineryBlockProperties;
import site.siredvin.progressiveperipherals.common.setup.Blocks;

public class BlockEvents {
    @SubscribeEvent
    public static void flexibleStatuePlaced(final BlockEvent.EntityPlaceEvent event) {
        if (event.getState().is(Blocks.FLEXIBLE_STATUE.get())) {
            IWorld world = event.getWorld();
            BlockPos below = event.getPos().below();
            BlockState belowBlock = world.getBlockState(below);
            if (belowBlock.is(Blocks.STATUE_WORKBENCH.get())) {
                world.setBlock(below, belowBlock.setValue(MachineryBlockProperties.CONNECTED, true), 3);
            }
        }
    }

    @SubscribeEvent
    public static void flexibleStatueRemoved(final BlockEvent.BreakEvent event) {
        if (event.getState().is(Blocks.FLEXIBLE_STATUE.get())) {
            IWorld world = event.getWorld();
            BlockPos below = event.getPos().below();
            BlockState belowBlock = world.getBlockState(below);
            if (belowBlock.is(Blocks.STATUE_WORKBENCH.get())) {
                world.setBlock(below, belowBlock.setValue(MachineryBlockProperties.CONNECTED, false), 3);
            }
        }
    }

    public static void subscribe(IEventBus bus) {
        bus.addListener(BlockEvents::flexibleStatuePlaced);
        bus.addListener(BlockEvents::flexibleStatueRemoved);
    }
}
