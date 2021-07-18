package site.siredvin.progressiveperipherals.integrations.top;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import site.siredvin.progressiveperipherals.ProgressivePeripherals;
import site.siredvin.progressiveperipherals.api.integrations.IProbeable;

public class ModProbeInfoProvider implements IProbeInfoProvider {

    @Override
    public String getID() {
        return ProgressivePeripherals.MOD_ID + ":" + "basicprovider";
    }

    @Override
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo iProbeInfo, PlayerEntity playerEntity, World world, BlockState blockState, IProbeHitData iProbeHitData) {
        final TileEntity tile = world.getBlockEntity(iProbeHitData.getPos());

        if (tile instanceof IProbeable) {
            for (ITextComponent text: ((IProbeable) tile).commonProbeData(blockState))
                iProbeInfo.text(text);
            if (probeMode != ProbeMode.NORMAL) {
                for (ITextComponent text: ((IProbeable) tile).extraProbeData(blockState))
                    iProbeInfo.text(text);
            }
        }
    }
}
