package site.siredvin.progressiveperipherals.extra.network;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.extra.network.api.EnderwireElementCategory;
import site.siredvin.progressiveperipherals.extra.network.api.EnderwireElementType;
import site.siredvin.progressiveperipherals.extra.network.api.NetworkAmplifier;

import java.util.Objects;

public class EnderwireNetworkElement {
    private static final String POS_TAG = "blockPosition";
    private static final String NAME_TAG = "name";
    private static final String CATEGORY_TAG = "category";
    private static final String ELEMENT_TYPE_TAG = "elementType";
    private static final String DIMENSION_TAG = "dimension";
    private static final String NETWORK_AMPLIFIER_TAG = "networkAmplifier";

    private final @NotNull String name;
    private final @NotNull BlockPos pos;
    private final @NotNull EnderwireElementCategory category;
    private final @NotNull EnderwireElementType elementType;
    private final @NotNull String dimension;
    private final @NotNull NetworkAmplifier networkAmplifier;

    public EnderwireNetworkElement(@NotNull String name, @NotNull BlockPos pos, @NotNull EnderwireElementCategory category, @NotNull EnderwireElementType elementType, @NotNull String dimension, @NotNull NetworkAmplifier networkAmplifier) {
        this.name = name;
        this.pos = pos;
        this.category = category;
        this.elementType = elementType;
        this.dimension = dimension;
        this.networkAmplifier = networkAmplifier;
    }

    public @NotNull BlockPos getPos() {
        return pos;
    }

    public @NotNull String getName() {
        return name;
    }

    public @NotNull EnderwireElementCategory getCategory() {
        return category;
    }

    public @NotNull EnderwireElementType getElementType() {
        return elementType;
    }

    public @NotNull NetworkAmplifier getNetworkAmplifier() {
        return networkAmplifier;
    }

    public @NotNull String getDimension() {
        return dimension;
    }

    public CompoundNBT toNBT() {
        CompoundNBT tag = new CompoundNBT();
        tag.putString(NAME_TAG, name);
        tag.put(POS_TAG, NBTUtil.writeBlockPos(pos));
        tag.putString(CATEGORY_TAG, category.name());
        tag.putString(ELEMENT_TYPE_TAG, elementType.name());
        tag.putString(DIMENSION_TAG, dimension);
        tag.putString(NETWORK_AMPLIFIER_TAG, networkAmplifier.name());
        return tag;
    }

    public static EnderwireNetworkElement fromCompound(CompoundNBT tag) {
        return new EnderwireNetworkElement(
                tag.getString(NAME_TAG),
                NBTUtil.readBlockPos(tag.getCompound(POS_TAG)),
                EnderwireElementCategory.valueOf(tag.getString(CATEGORY_TAG)),
                EnderwireElementType.valueOf(tag.getString(ELEMENT_TYPE_TAG)),
                tag.getString(DIMENSION_TAG),
                NetworkAmplifier.valueOf(tag.getString(NETWORK_AMPLIFIER_TAG))
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EnderwireNetworkElement)) return false;
        EnderwireNetworkElement that = (EnderwireNetworkElement) o;
        return name.equals(that.name) && pos.equals(that.pos) && category.equals(that.category) && elementType.equals(that.elementType) && dimension.equals(that.dimension) && networkAmplifier == that.networkAmplifier;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, pos, category, elementType, dimension, networkAmplifier);
    }
}
