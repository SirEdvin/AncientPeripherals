package site.siredvin.progressiveperipherals.extra.network;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleSide;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.extra.network.api.*;
import site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.enderwire.EnderwireTurtleUpgradeModemPeripheral;

import java.util.Objects;

public class TurtleEnderwireNetworkElement implements IEnderwireNetworkElement {
    public static final String TYPE_MARK = "moving";

    private final @NotNull ITurtleAccess turtle;
    private final @NotNull TurtleSide side;
    private final @NotNull String name;
    private final @NotNull EnderwireElementCategory category;
    private final @NotNull EnderwireElementType elementType;
    private final @NotNull NetworkAmplifier networkAmplifier;

    public TurtleEnderwireNetworkElement(@NotNull String name, @NotNull ITurtleAccess turtle, @NotNull TurtleSide side, @NotNull EnderwireElementCategory category, @NotNull EnderwireElementType elementType, @NotNull String dimension, @NotNull NetworkAmplifier networkAmplifier) {
        this.turtle = turtle;
        this.side = side;
        this.name = name;
        this.category = category;
        this.elementType = elementType;
        this.networkAmplifier = networkAmplifier;
    }

    @Override
    public @NotNull BlockPos getPos() {
        return turtle.getPosition();
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public @NotNull EnderwireElementCategory getCategory() {
        return category;
    }

    @Override
    public @NotNull EnderwireElementType getElementType() {
        return elementType;
    }

    @Override
    public @NotNull NetworkAmplifier getNetworkAmplifier() {
        return networkAmplifier;
    }

    @Override
    public @NotNull String getDimension() {
        return Objects.requireNonNull(turtle.getWorld()).dimension().location().toString();
    }

    @Override
    public @NotNull CompoundNBT toNBT() {
        throw new IllegalArgumentException("This method shouldn't be called, actually, because this element is not marked as stable");
    }

    @Override
    public @NotNull String getTypeMark() {
        return TYPE_MARK;
    }

    @Override
    public @Nullable IEnderwireElement getElement(World world) {
        IPeripheral peripheral = turtle.getPeripheral(side);
        if (peripheral instanceof EnderwireTurtleUpgradeModemPeripheral)
            return ((EnderwireTurtleUpgradeModemPeripheral) peripheral).getEnderwireElement();
        return null;
    }

    @Override
    public boolean isStable() {
        return false;
    }
}
