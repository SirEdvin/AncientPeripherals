package site.siredvin.progressiveperipherals.api.tileentity;

import javax.annotation.Nonnull;
import java.awt.*;

public interface IRealityBreakthroughPointTileEntity {
    @Nonnull Color getColor();
    @Nonnull IRealityBreakthroughPointTier getTier();
}
