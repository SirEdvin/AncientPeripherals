package site.siredvin.progressiveperipherals.extra.network.api;

import net.minecraft.tileentity.TileEntity;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.api.base.ITrickedTileEntity;
import site.siredvin.progressiveperipherals.extra.network.PlacedEnderwireNetworkElement;

import java.util.Objects;

public interface IEnderwireTileEntity<T extends TileEntity & IEnderwireTileEntity<T>> extends IEnderwireElement, ITrickedTileEntity<T> {
    @Override
    @NotNull
    default IEnderwireNetworkElement generateElementData(@NotNull String elementName) {
        return new PlacedEnderwireNetworkElement(
                elementName, getPosition(), getElementType().getCategory(), getElementType(),
                Objects.requireNonNull(getWorld()).dimension().location().toString(), getAmplifier()
        );
    }
}
