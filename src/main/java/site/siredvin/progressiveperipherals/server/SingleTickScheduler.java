package site.siredvin.progressiveperipherals.server;

import com.google.common.collect.MapMaker;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import site.siredvin.progressiveperipherals.ProgressivePeripherals;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

@Mod.EventBusSubscriber(modid = ProgressivePeripherals.MOD_ID)
public final class SingleTickScheduler {
    /**
     * This nice class allow to perform one (!) blockTick for pushed tile
     */

    private static final Set<TileEntity> toTick = Collections.newSetFromMap(new MapMaker().weakKeys().makeMap());

    public static void schedule(TileEntity tile) {
        World world = tile.getLevel();
        if (world != null && !world.isClientSide)
            toTick.add(tile);
    }

    public static void now(TileEntity tile) {
        World world = tile.getLevel();
        if (world != null && !world.isClientSide)
            world.getBlockTicks().scheduleTick(tile.getBlockPos(), tile.getBlockState().getBlock(), 0);
    }

    @SubscribeEvent
    public static void tick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;

        Iterator<TileEntity> iterator = toTick.iterator();
        while (iterator.hasNext()) {
            TileEntity tile = iterator.next();
            iterator.remove();

            World world = tile.getLevel();
            BlockPos pos = tile.getBlockPos();

            if (world != null && world.isAreaLoaded(pos, 0) && world.getBlockEntity(pos) == tile) {
                world.getBlockTicks().scheduleTick(pos, tile.getBlockState().getBlock(), 0);
            }
        }
    }
}
