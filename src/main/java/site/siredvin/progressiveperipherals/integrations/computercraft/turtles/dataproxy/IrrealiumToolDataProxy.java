package site.siredvin.progressiveperipherals.integrations.computercraft.turtles.dataproxy;

import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleSide;
import de.srendi.advancedperipherals.lib.peripherals.owner.IPeripheralOwner;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.integrations.computercraft.turtles.base.TurtleDigOperationType;
import site.siredvin.progressiveperipherals.integrations.computercraft.turtles.base.UltimineMode;

import java.util.List;
import java.util.stream.Collectors;

public class IrrealiumToolDataProxy {
    private static final String VOIDING_MARK = "voiding";
    private static final String VOIDING_TAGS_MARK = "voidingTags";
    private static final String ULTIMINE_MOD_MARK = "ultimineMod";

    public static UltimineMode getUltimineMode(@NotNull ITurtleAccess turtle, @NotNull TurtleSide side) {
        CompoundNBT data = turtle.getUpgradeNBTData(side);
        if (data.contains(ULTIMINE_MOD_MARK))
            return UltimineMode.valueOf(data.getString(ULTIMINE_MOD_MARK));
        return UltimineMode.NONE;
    }


    public static UltimineMode getUltimineMode(@NotNull IPeripheralOwner owner) {
        CompoundNBT data = owner.getDataStorage();
        if (data.contains(ULTIMINE_MOD_MARK))
            return UltimineMode.valueOf(data.getString(ULTIMINE_MOD_MARK));
        return UltimineMode.NONE;
    }

    public static void setUltimineMod(@NotNull IPeripheralOwner owner, @NotNull UltimineMode mode) {
        CompoundNBT data = owner.getDataStorage();
        data.putString(ULTIMINE_MOD_MARK, mode.toString());
        owner.markDataStorageDirty();
    }

    public static boolean isVoiding(@NotNull ITurtleAccess turtle, @NotNull TurtleSide side) {
        return turtle.getUpgradeNBTData(side).getBoolean(VOIDING_MARK);
    }

    public static boolean isVoiding(@NotNull IPeripheralOwner owner) {
        return owner.getDataStorage().getBoolean(VOIDING_MARK);
    }

    public static void setVoiding(@NotNull IPeripheralOwner owner, boolean value) {
        owner.getDataStorage().putBoolean(VOIDING_MARK, value);
        owner.markDataStorageDirty();
    }

    public static List<String> getVoidingTags(@NotNull ITurtleAccess turtle, @NotNull TurtleSide side) {
        return turtle.getUpgradeNBTData(side).getList(VOIDING_TAGS_MARK, 8).stream().map(INBT::getAsString).collect(Collectors.toList());
    }

    public static List<String> getVoidingTags(@NotNull IPeripheralOwner owner) {
        return owner.getDataStorage().getList(VOIDING_TAGS_MARK, 8).stream().map(INBT::getAsString).collect(Collectors.toList());
    }

    public static void clearVoidingTags(@NotNull IPeripheralOwner owner) {
        owner.getDataStorage().remove(VOIDING_TAGS_MARK);
        owner.markDataStorageDirty();
    }

    public static void addToVoidingTags(@NotNull IPeripheralOwner owner, String tag) {
        CompoundNBT dataStorage = owner.getDataStorage();
        ListNBT list = dataStorage.getList(VOIDING_TAGS_MARK, 8);
        list.add(StringNBT.valueOf(tag));
        dataStorage.put(VOIDING_TAGS_MARK, list);
        owner.markDataStorageDirty();
    }

    public static void removeFromVoidingTags(@NotNull IPeripheralOwner owner, String tag) {
        CompoundNBT dataStorage = owner.getDataStorage();
        ListNBT list = dataStorage.getList(VOIDING_TAGS_MARK, 8);
        list.removeIf(el -> el.getAsString().equals(tag));
        dataStorage.put(VOIDING_TAGS_MARK, list);
        owner.markDataStorageDirty();
    }

    public static TurtleDigOperationType getOperationType(@NotNull ITurtleAccess turtle, @NotNull TurtleSide side) {
        boolean isUltimine = IrrealiumToolDataProxy.getUltimineMode(turtle, side) != UltimineMode.NONE;
        boolean isVoiding = IrrealiumToolDataProxy.isVoiding(turtle, side);
        return getOperationType(isUltimine, isVoiding);
    }

    public static TurtleDigOperationType getOperationType(@NotNull IPeripheralOwner owner) {
        boolean isUltimine = IrrealiumToolDataProxy.getUltimineMode(owner) != UltimineMode.NONE;
        boolean isVoiding = IrrealiumToolDataProxy.isVoiding(owner);
        return getOperationType(isUltimine, isVoiding);
    }

    protected static TurtleDigOperationType getOperationType(boolean isUltimine, boolean isVoiding) {
        if (isUltimine && isVoiding)
            return TurtleDigOperationType.VOIDING_ULTIMINE;
        if (isUltimine)
            return TurtleDigOperationType.ULTIMINE;
        if (isVoiding)
            return TurtleDigOperationType.VOIDING;
        return TurtleDigOperationType.SINGLE;
    }
}
