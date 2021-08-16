package site.siredvin.progressiveperipherals.integrations.computercraft.turtles.base;

import dan200.computercraft.api.client.TransformedModel;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.AbstractTurtleUpgrade;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleSide;
import dan200.computercraft.api.turtle.TurtleUpgradeType;
import de.srendi.advancedperipherals.common.addons.computercraft.base.DisabledPeripheral;
import de.srendi.advancedperipherals.common.addons.computercraft.base.IBasePeripheral;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public abstract class DataDependPeripheralTurtle<T extends IBasePeripheral> extends AbstractTurtleUpgrade {

    protected static final Map<ModelResourceLocation, TransformedModel> _TRANSFORMED_MODEL_CACHE = new HashMap<>();

    @SuppressWarnings("SameParameterValue")
    protected DataDependPeripheralTurtle(ResourceLocation id, String adjective, ItemStack stack) {
        super(id, TurtleUpgradeType.PERIPHERAL, adjective, stack);
    }

    protected @NotNull abstract TransformedModel getLeftModel(@Nullable ITurtleAccess turtle, @NotNull TurtleSide side);

    protected @NotNull abstract TransformedModel getRightModel(@Nullable ITurtleAccess turtle, @NotNull TurtleSide side);

    protected abstract T buildPeripheral(@NotNull ITurtleAccess turtle, @NotNull TurtleSide side);

    @NotNull
    @Override
    public TransformedModel getModel(@Nullable ITurtleAccess turtle, @NotNull TurtleSide side) {
        if (side == TurtleSide.LEFT)
            return getLeftModel(turtle, side);
        return getRightModel(turtle,side);
    }

    @Nullable
    @Override
    public IPeripheral createPeripheral(@NotNull ITurtleAccess turtle, @NotNull TurtleSide side) {
        T peripheral = buildPeripheral(turtle, side);
        if (!peripheral.isEnabled()) {
            return DisabledPeripheral.INSTANCE;
        }
        return peripheral;
    }

    protected static TransformedModel of(ModelResourceLocation location) {
        if (!_TRANSFORMED_MODEL_CACHE.containsKey(location))
            _TRANSFORMED_MODEL_CACHE.put(location, TransformedModel.of(location));
        return _TRANSFORMED_MODEL_CACHE.get(location);
    }
}
