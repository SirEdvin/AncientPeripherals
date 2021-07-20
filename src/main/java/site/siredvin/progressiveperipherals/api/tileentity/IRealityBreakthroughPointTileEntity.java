package site.siredvin.progressiveperipherals.api.tileentity;

import org.jetbrains.annotations.NotNull;

import java.awt.*;

public interface IRealityBreakthroughPointTileEntity {
    @NotNull Color getColor();
    @NotNull IRealityBreakthroughPointTier getTier();
}
