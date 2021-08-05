package site.siredvin.progressiveperipherals.common.blocks.enderwire;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.common.tileentities.enderwire.EnderwireLightEmitterTileEntity;

import java.awt.*;

public class EnderwireLightEmitterBlockColor  implements IBlockColor {
    private static final Color DEFAULT_COLOR = Color.WHITE;

    @Override
    public int getColor(@NotNull BlockState state, @Nullable IBlockDisplayReader displayReader, @Nullable BlockPos pos, int tintIndex) {
        if (pos == null)
            return DEFAULT_COLOR.getRGB();
        World world = Minecraft.getInstance().level;
        if (world == null)
            return DEFAULT_COLOR.getRGB();
        EnderwireLightEmitterTileEntity te = (EnderwireLightEmitterTileEntity) world.getBlockEntity(pos);
        if (te == null)
            return DEFAULT_COLOR.getRGB();
        if (state.getValue(EnderwireLightEmitterBlock.ENABLED))
            return te.getColor().getRGB();
        return te.getColor().darker().darker().getRGB();
    }
}
