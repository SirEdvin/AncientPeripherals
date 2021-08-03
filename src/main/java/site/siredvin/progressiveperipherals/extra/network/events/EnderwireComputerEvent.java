package site.siredvin.progressiveperipherals.extra.network.events;

import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class EnderwireComputerEvent implements IEpochEvent {
    private final @NotNull String name;
    private final @NotNull Map<String, Object> data;
    private final int reachableRange;
    private final boolean interdimensional;
    private final String originalDimension;
    private final BlockPos pos;
    private final long epoch;


    public EnderwireComputerEvent(@NotNull String name, int reachableRange, boolean interdimensional, @NotNull String originalDimension, @NotNull BlockPos pos, @NotNull Map<String, Object> data, long epoch) {
        this.name = name;
        this.reachableRange = reachableRange;
        this.interdimensional = interdimensional;
        this.originalDimension = originalDimension;
        this.pos = pos;
        this.data = data;
        this.epoch = epoch;
    }

    public @NotNull String getName() {
        return name;
    }

    public @NotNull Map<String, Object> getData() {
        return data;
    }

    public long getEpoch() {
        return epoch;
    }

    public boolean isValid(BlockPos receiverPos, String receiverDimension) {
        return reachableRange * reachableRange <= receiverPos.distSqr(pos) && (receiverDimension.equals(originalDimension) || interdimensional);
    }

    public static EnderwireComputerEvent timed(@NotNull String name, int reachableRange, boolean interdimensional, @NotNull String originalDimension, @NotNull BlockPos pos, Map<String, Object> data) {
        LocalDateTime now = LocalDateTime.now();
        long epoch = now.toEpochSecond(ZoneOffset.UTC);

        data.put("datetime", now.format(DateTimeFormatter.ISO_DATE_TIME));
        data.put("epoch", epoch);
        data.put("event", name);

        return new EnderwireComputerEvent(name, reachableRange, interdimensional, originalDimension, pos, data, epoch);
    }
}
