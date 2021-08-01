package site.siredvin.progressiveperipherals.extra.network.events;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class EnderwireComputerEvent implements IEpochEvent {
    private final @NotNull EnderwireComputerEventType type;
    private final @NotNull Object[] data;
    private final long epoch;


    public EnderwireComputerEvent(@NotNull EnderwireComputerEventType type, @NotNull Object[] data, long epoch) {
        this.type = type;
        this.data = data;
        this.epoch = epoch;
    }

    public EnderwireComputerEvent(@NotNull EnderwireComputerEventType type) {
        this(type, new Object[0], LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
    }

    public @NotNull EnderwireComputerEventType getType() {
        return type;
    }

    public @NotNull String getName() {
        return type.computerName();
    }

    public @NotNull Object[] getData() {
        return data;
    }

    public long getEpoch() {
        return epoch;
    }

    public static EnderwireComputerEvent timed(@NotNull EnderwireComputerEventType type, Object... data) {
        Object[] eventData = new Object[data.length + 1];
        long epoch = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        eventData[0] = epoch;
        System.arraycopy(data, 0, eventData, 1, data.length);
        return new EnderwireComputerEvent(type, eventData, epoch);
    }
}
