package site.siredvin.progressiveperipherals.extra.network.tools;

import net.minecraft.nbt.CompoundNBT;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.extra.network.PlacedEnderwireNetworkElement;
import site.siredvin.progressiveperipherals.extra.network.api.IEnderwireNetworkElement;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class NetworkElementSerializedTool {
    private static final String TYPE_MARK_TAG = "__type_mark_tag__";

    private static final Map<String, Function<CompoundNBT, IEnderwireNetworkElement>> DESERIALIZATION_MAP = new HashMap<>();

    private static final Function<CompoundNBT, IEnderwireNetworkElement> FALLBACK_DESERIALIZATION = PlacedEnderwireNetworkElement::fromCompound;;

    static {
        DESERIALIZATION_MAP.put(PlacedEnderwireNetworkElement.TYPE_MARK, PlacedEnderwireNetworkElement::fromCompound);
    }

    public static @NotNull IEnderwireNetworkElement deserialize(@NotNull CompoundNBT tag) {
        String typeMark = tag.getString(TYPE_MARK_TAG);
        if (!DESERIALIZATION_MAP.containsKey(typeMark))
            return FALLBACK_DESERIALIZATION.apply(tag);
        return DESERIALIZATION_MAP.get(typeMark).apply(tag);
    }

    public static @NotNull CompoundNBT serialize(@NotNull IEnderwireNetworkElement element) {
        CompoundNBT tag = element.toNBT();
        tag.putString(TYPE_MARK_TAG, element.getTypeMark());
        return tag;
    }
}
