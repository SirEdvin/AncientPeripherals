package site.siredvin.progressiveperipherals.common.tileentities;

import de.srendi.advancedperipherals.common.blocks.base.PeripheralTileEntity;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.common.setup.TileEntityTypes;
import site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.RecipeRegistryPeripheral;

public class RecipeRegistryTileEntity extends PeripheralTileEntity<RecipeRegistryPeripheral> {
    public RecipeRegistryTileEntity() {
        super(TileEntityTypes.RECIPE_REGISTRY.get());
    }

    @Override
    protected @NotNull RecipeRegistryPeripheral createPeripheral() {
        return new RecipeRegistryPeripheral("recipeRegistry",this);
    }
}
