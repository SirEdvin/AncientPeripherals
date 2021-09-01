package site.siredvin.progressiveperipherals.common.tileentities;

import de.srendi.advancedperipherals.common.blocks.base.PeripheralTileEntity;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.common.setup.TileEntityTypes;
import site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.ItemRegistryPeripheral;

public class ItemRegistryTileEntity extends PeripheralTileEntity<ItemRegistryPeripheral> {

    public ItemRegistryTileEntity() {
        super(TileEntityTypes.ITEM_REGISTRY.get());
    }

    @Override
    protected @NotNull ItemRegistryPeripheral createPeripheral() {
        return new ItemRegistryPeripheral(this);
    }
}
